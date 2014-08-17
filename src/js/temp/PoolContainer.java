package js.data;

public class PoolContainer<T extends PoolItem> implements PoolInterface<T>
{
	private PoolFactory<T> factory = null;

	private PoolItem free = new PoolItem();
	private PoolItem used = new PoolItem();

	private int freeCount = 0;
	private int usedCount = 0;
	private int allocateCount = 0;
	private int releaseCount = 0;
	private int createCount = 0;

	public PoolContainer(PoolFactory<T> factory)
	{
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see js.data.PoolInterface#allocate()
	 */
	@Override
	public T allocate()
	{
		PoolItem item = null;

		if (free.next == null)
		{
			item = factory.create();
			createCount++;
		}
		else
		{
			item = free.next;
			item.remove();
			freeCount--;
		}

		used.insert(item);
		usedCount++;

		allocateCount++;

		return (T) item;
	}

	/* (non-Javadoc)
	 * @see js.data.PoolInterface#release(js.data.PoolItem)
	 */
	@Override
	public void release(PoolItem item)
	{
		item.remove();
		usedCount--;
		free.insert(item);
		freeCount++;
		releaseCount++;
	}

	/* (non-Javadoc)
	 * @see js.data.PoolInterface#iterate(js.data.PoolItemAction)
	 */
	@Override
	public void iterate(PoolItemAction<T> action)
	{
		PoolItem item = used.next;

		while (item != null)
		{
			if (action.act((T) item) == false)
			{
				item = item.prev;
				release(item.next);
			}
			item = item.next;
		}
	}
	
	/* (non-Javadoc)
	 * @see js.data.PoolInterface#info()
	 */
	@Override
	public String info()
	{
		return "[freeCount=" + freeCount + ", usedCount=" + usedCount + ", diff=" + (allocateCount - releaseCount)
				+ ", createCount=" + createCount + "]";
	}

	public interface PoolFactory<T>
	{
		T create();
	}

}
