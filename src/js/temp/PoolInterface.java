package js.data;

public interface PoolInterface<T extends PoolItem>
{

	public abstract T allocate();

	public abstract void release(PoolItem item);

	public abstract void iterate(PoolItemAction<T> action);

	public abstract String info();

}