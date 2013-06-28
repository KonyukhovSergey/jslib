package js.math;

public class ViewPort
{
	public float dx = 0;
	public float dy = 0;
	public float sx = 1;
	public float sy = 1;
	public float dist = 10;

	public ViewPort()
	{
	}

	public final void set(int width, int height, float length)
	{
		// float acpect = (float) height / (float) width;

		sx = dist * (float) width / length;
		sy = sx;

		dx = (float) width / 2;
		dy = (float) width / 2;
	}


	public final float x(Vector3D v)
	{
		return (v.x / (v.z + dist)) * sx + dx;
	}

	public final float y(Vector3D v)
	{
		return (v.y / (v.z + dist)) * sy + dy;
	}
}
