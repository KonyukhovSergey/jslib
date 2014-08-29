package js.data;

import java.util.Iterator;

public class ObjectPool<T> implements Iterable<T>
{
	private PoolItem<T> free = new PoolItem<T>();
	private PoolItem<T> used = new PoolItem<T>();

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
		PoolItem<T> item = null;

		if (free.next == null)
		{
			item = new PoolItem<T>();
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

	private static final class PoolIterator<T> implements Iterator<T>
	{
		PoolItem<T> item;
		PoolItem<T> free;

		public PoolIterator(PoolItem<T> free)
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
			return item.data;
		}

		@Override
		public void remove()
		{
			PoolItem<T> temp = item;
			item = item.prev;
			temp.remove();
			free.insert(temp);
		}
	}

	private static final class PoolItem<T>
	{
		T data;

		PoolItem<T> prev, next;

		final void insert(PoolItem<T> item)
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

	public static interface ObjectFactory<T>
	{
		T create();
	}
}
