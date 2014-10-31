package js.math;


public class Vector3D
{
	public float x;
	public float y;
	public float z;
	public float t = 1;

	public Vector3D()
	{
	}

	public Vector3D(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D(Vector3D v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public final Vector3D clone()
	{
		return new Vector3D(x, y, z);
	}

	public final void plus(Vector3D v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public final void plus(float x, float y, float z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public final void plus(float x, float y, float z, float s)
	{
		this.x += x * s;
		this.y += y * s;
		this.z += z * s;
	}

	public final void plus(Vector3D v, float s)
	{
		x += v.x * s;
		y += v.y * s;
		z += v.z * s;
	}

	public final void plus(Vector3D a, Vector3D b)
	{
		x = a.x + b.x;
		y = a.y + b.y;
		z = a.z + b.z;
	}

	public final void minus(Vector3D v)
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public final void minus(Vector3D v, float s)
	{
		x -= v.x * s;
		y -= v.y * s;
		z -= v.z * s;
	}

	public final void minus(Vector3D a, Vector3D b)
	{
		x = a.x - b.x;
		y = a.y - b.y;
		z = a.z - b.z;
	}

	public final void negative()
	{
		x = -x;
		y = -y;
		z = -z;
	}

	public final float length()
	{
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public final float quadLength()
	{
		return x * x + y * y + z * z;
	}

	public final void normalize()
	{
		float length = length();

		if (MathUtils.isZero(length) == false)
		{
			x /= length;
			y /= length;
			z /= length;
		}
		else
		{
			x = 0;
			y = 0;
			z = 0;
		}
	}

	public final void scale(float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}

	public final void interpolate(Vector3D a, Vector3D b, float position)
	{
		x = (b.x - a.x) * position + a.x;
		y = (b.y - a.y) * position + a.y;
		z = (b.z - a.z) * position + a.z;
	}

	public final void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final void set(Vector3D a)
	{
		this.x = a.x;
		this.y = a.y;
		this.z = a.z;
	}

	public final float scalar(Vector3D b)
	{
		return x * b.x + y * b.y + z * b.z;
	}

	public final void dotProduct(Vector3D a, Vector3D b)
	{
		x = a.y * b.z - a.z * b.y;
		y = a.z * b.x - a.x * b.z;
		z = a.x * b.y - a.y * b.x;
	}

	public final float cosFi(Vector3D b)
	{
		float l1 = quadLength();

		if (MathUtils.isZero(l1))
		{
			return 0;
		}

		float l2 = b.quadLength();

		if (MathUtils.isZero(l2))
		{
			return 0;
		}

		return scalar(b) / ((float) (Math.sqrt(l1) * Math.sqrt(l2)));
	}

	public final void multiply(Vector3D v, Matrix3D a)
	{
		float[] m = a.m;

		x = v.x * m[0] + v.y * m[1] + v.z * m[2] + v.t * m[3];
		y = v.x * m[4] + v.y * m[5] + v.z * m[6] + v.t * m[7];
		z = v.x * m[8] + v.y * m[9] + v.z * m[10] + v.t * m[11];
		t = v.x * m[12] + v.y * m[13] + v.z * m[14] + v.t * m[15];
	}

	public final void multiply(Matrix3D a, Vector3D v)
	{
		float[] m = a.m;

		x = v.x * m[0] + v.y * m[4] + v.z * m[8] + v.t * m[12];
		y = v.x * m[1] + v.y * m[5] + v.z * m[9] + v.t * m[13];
		z = v.x * m[2] + v.y * m[6] + v.z * m[10] + v.t * m[14];
		t = v.x * m[3] + v.y * m[7] + v.z * m[11] + v.t * m[15];
	}

	@Override
	public String toString()
	{
		return String.format("%3.3f %3.3f %3.3f", x, y, z);
	}
}
