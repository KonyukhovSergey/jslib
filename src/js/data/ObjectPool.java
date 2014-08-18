package js.data;

import java.util.Iterator;

public class ObjectPool<T> implements Iterable<T>
{
	private PoolItem free = new PoolItem();
	private PoolItem used = new PoolItem();

	private ObjectFactory<T> factory = null;

	private PoolIterator<T> iterator;

	public ObjectPool(ObjectFactory<T> factory)
	{
		this.factory = factory;
		iterator = new PoolIterator<T>(free);
	}

	@Override
	public Iterator<T> iterator()
	{
		iterator.item = used;
		return iterator;
	}

	public T allocate()
	{
		PoolItem item = null;

		if (free.next == null)
		{
			item = new PoolItem();
			item.data = factory.create();
		}
		else
		{
			item = free.next;
			item.remove();
		}

		used.insert(item);

		return item.data;
	}

	private class PoolIterator<T> implements Iterator<T>
	{
		PoolItem item;
		PoolItem free;

		public PoolIterator(PoolItem free)
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
			return (T) item.data;
		}

		@Override
		public void remove()
		{
			PoolItem temp = item;
			item = item.prev;
			temp.remove();
			free.insert(temp);
		}
	}

	private class PoolItem
	{
		T data;

		PoolItem prev, next;

		final void insert(PoolItem item)
		{
			item.prev = this;
			item.next = next;

			if (next != null)
			{
				next.prev = item;
			}

			next = item;
		}

		final void remove()
		{
			if (prev != null)
			{
				prev.next = next;
			}

			if (next != null)
			{
				next.prev = prev;
			}

			prev = null;
			next = null;
		}
	}

	public interface ObjectFactory<T>
	{
		T create();
	}
}
