package js.data;

import java.util.Iterator;

public class ObjectPool<T extends PoolItem> implements Iterable<T>
{
	private PoolFactory<T> factory = null;

	private PoolItem free = new PoolItem();
	private PoolItem used = new PoolItem();

	private ItemsIterator<T> iterator = new ItemsIterator<T>();

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
		}
		else
		{
			item = free.next;
			item.remove();
		}

		used.insert(item);

		return (T) item;
	}

	public void release(PoolItem item)
	{
		item.remove();
		free.insert(item);
	}

	public interface PoolFactory<T>
	{
		T create();
	}

	@Override
	public Iterator<T> iterator()
	{
		iterator.item = used;
		return iterator;
	}

	private static class ItemsIterator<T> implements Iterator<T>
	{
		PoolItem item;

		@Override
		public boolean hasNext()
		{
			return item.next != null;
		}

		@Override
		public T next()
		{
			item = item.next;
			return (T) item.prev;
		}

		@Override
		public void remove()
		{
			item.remove();
		}

	}

}
