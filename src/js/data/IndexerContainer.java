package js.data;


public class IndexerContainer<T extends IndexerItem>
{
	private T free = null;
	private T used = null;
	private Class<? extends T> classType;

	public IndexerContainer(Class<? extends T> classType)
	{
		this.classType = classType;

		try
		{
			free = classType.newInstance();
			used = classType.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	public final T allocate()
	{
		@SuppressWarnings("unchecked")
		T item = (T) free.next();

		if (item == null)
		{
			try
			{
				item = classType.newInstance();
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			item.remove();
		}

		used.insert(item);

		return item;
	}

	public final void release(T item)
	{
		item.remove();

		free.insert(item);
	}

	@SuppressWarnings("unchecked")
	public final T getFirst()
	{
		return (T) used.next();
	}
}
