package js.math;

public class MathUtils
{
	private static final float EPSILON = 0.00001f;

	public static final boolean isZero(float value)
	{
		return Math.abs(value) < EPSILON;
	}

	public static final boolean isEqual(float a, float b)
	{
		return Math.abs(a - b) < EPSILON;
	}

	public static float interpolate(float v1, float v2, float p)
	{
		return v1 * (1.0f - p) + v2 * p;
	}

	public static float interpolate(float v1, float v2, float v3, float v4, float px, float py)
	{
		return (v1 * (1.0f - px) + v2 * px) * (1.0f - py) + (v3 * (1.0f - px) + v4 * px) * py;
	}

	public static float length(float x, float y, float z)
	{
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public static float length(float x, float y)
	{
		return (float) Math.sqrt(x * x + y * y);
	}

	public static float quad(float x, float y)
	{
		return x * x + y * y;
	}
}
