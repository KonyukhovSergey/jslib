package js.data;

public class IndexerItem
{
	private IndexerItem next;
	private IndexerItem prev;

	public IndexerItem()
	{
		next = null;
		prev = null;
	}

	public final IndexerItem next()
	{
		return next;
	}

	public final IndexerItem prev()
	{
		return prev;
	}

	/**
	 * insert new item after this item
	 * 
	 * @param item
	 */
	public final void insert(IndexerItem item)
	{
		item.prev = this;
		item.next = next;

		if (next != null)
		{
			next.prev = item;
		}

		next = item;
	}

	public final IndexerItem remove()
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

		return this;
	}
}
