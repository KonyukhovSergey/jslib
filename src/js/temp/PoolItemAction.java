package js.data;

public interface PoolItemAction<T>
{
	boolean act(T item);
}
