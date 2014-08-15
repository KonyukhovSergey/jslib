package js.data;

public class PoolItem
{
	public PoolItem prev, next;

	public final void insert(PoolItem item)
	{
		item.prev = this;
		item.next = next;

		if (next != null)
		{
			next.prev = item;
		}

		next = item;
	}

	public final void remove()
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