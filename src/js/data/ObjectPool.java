package js.data;

import java.util.Iterator;

import android.util.Log;

public class ObjectPool<T extends PoolItem> implements Iterable<T>
{
	private PoolFactory<T> factory = null;

	private PoolItem free = new PoolItem();
	private PoolItem used = new PoolItem();

	private int freeCount = 0;
	private int usedCount = 0;
	private int allocateCount = 0;
	private int releaseCount = 0;
	private int createCount = 0;

	private ItemsIterator<T> iterator = new ItemsIterator<T>(free);

	public ObjectPool(PoolFactory<T> factory)
	{
		this.factory = factory;
	}

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

	public void release(PoolItem item)
	{
		item.remove();
		usedCount--;
		free.insert(item);
		freeCount++;
		releaseCount++;
	}

	public interface PoolFactory<T>
	{
		T create();
	}

	@Override
	public Iterator<T> iterator()
	{
		// Log.d("pool", "get iterator")
		iterator.item = used;
		
		return iterator;
	}

	private class ItemsIterator<T> implements Iterator<T>
	{
		PoolItem item;
		PoolItem free;
		
		public ItemsIterator(PoolItem free)
		{
			this.free = free;
		}

		@Override
		public boolean hasNext()
		{
			return item.next != null;
		}

		@Override
		public T next()
		{
			item = item.next;
			return (T) item;
		}

		@Override
		public void remove()
		{
			PoolItem temp = item;
			item = item.prev;
			temp.remove();
			usedCount--;
			free.insert(temp);
			freeCount++;
			releaseCount++;
		}
	}

	public String info()
	{
		return "[freeCount=" + freeCount + ", usedCount=" + usedCount + ", diff=" + (allocateCount - releaseCount)
				+ ", createCount=" + createCount + "]";
	}

}
