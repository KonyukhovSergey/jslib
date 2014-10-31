package js.math;


public class Matrix3D
{
	public float[] m = new float[16];

	public static final Matrix3D Z = new Matrix3D(new float[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
	public static final Matrix3D E = new Matrix3D(new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });

	private static final Matrix3D t1 = new Matrix3D();
	private static final Matrix3D t2 = new Matrix3D();

	public Matrix3D()
	{
		set(E);
	}

	public Matrix3D(float[] array)
	{
		System.arraycopy(array, 0, m, 0, m.length);
	}

	public final void plus(Matrix3D a)
	{
		for (int i = 0; i < m.length; i++)
		{
			m[i] += a.m[i];
		}
	}

	public final void plus(Matrix3D a, Matrix3D b)
	{
		for (int i = 0; i < m.length; i++)
		{
			m[i] = a.m[i] + b.m[i];
		}
	}

	public final void minus(Matrix3D a)
	{
		for (int i = 0; i < m.length; i++)
		{
			m[i] += a.m[i];
		}
	}

	public final void minus(Matrix3D a, Matrix3D b)
	{
		for (int i = 0; i < m.length; i++)
		{
			m[i] += a.m[i] - b.m[i];
		}
	}

	public final void negative()
	{
		for (int i = 0; i < m.length; i++)
		{
			m[i] = -m[i];
		}
	}

	public final void negative(Matrix3D a)
	{
		for (int i = 0; i < m.length; i++)
		{
			m[i] = -a.m[i];
		}
	}

	public final void transpose()
	{
		swap(1, 4);
		swap(2, 8);
		swap(3, 12);
		swap(6, 9);
		swap(7, 13);
		swap(11, 14);
	}

	public final void transpose(Matrix3D a)
	{
		set(a);
		transpose();
	}

	public final void multuply(Matrix3D a, Matrix3D b)
	{
		float[] x = a.m;
		float[] y = b.m;

		m[0] = x[0] * y[0] + x[1] * y[4] + x[2] * y[8] + x[3] * y[12];
		m[1] = x[0] * y[1] + x[1] * y[5] + x[2] * y[9] + x[3] * y[13];
		m[2] = x[0] * y[2] + x[1] * y[6] + x[2] * y[10] + x[3] * y[14];
		m[3] = x[0] * y[3] + x[1] * y[7] + x[2] * y[11] + x[3] * y[15];

		m[4] = x[4] * y[0] + x[5] * y[4] + x[6] * y[8] + x[7] * y[12];
		m[5] = x[4] * y[1] + x[5] * y[5] + x[6] * y[9] + x[7] * y[13];
		m[6] = x[4] * y[2] + x[5] * y[6] + x[6] * y[10] + x[7] * y[14];
		m[7] = x[4] * y[3] + x[5] * y[7] + x[6] * y[11] + x[7] * y[15];

		m[8] = x[8] * y[0] + x[9] * y[4] + x[10] * y[8] + x[11] * y[12];
		m[9] = x[8] * y[1] + x[9] * y[5] + x[10] * y[9] + x[11] * y[13];
		m[10] = x[8] * y[2] + x[9] * y[6] + x[10] * y[10] + x[11] * y[14];
		m[11] = x[8] * y[3] + x[9] * y[7] + x[10] * y[11] + x[11] * y[15];

		m[12] = x[12] * y[0] + x[13] * y[4] + x[14] * y[8] + x[15] * y[12];
		m[13] = x[12] * y[1] + x[13] * y[5] + x[14] * y[9] + x[15] * y[13];
		m[14] = x[12] * y[2] + x[13] * y[6] + x[14] * y[10] + x[15] * y[14];
		m[15] = x[12] * y[3] + x[13] * y[7] + x[14] * y[11] + x[15] * y[15];
	}

	public void set(float[] a, int offset)
	{
		System.arraycopy(a, offset, m, 0, a.length);
	}

	public void set(Matrix3D m)
	{
		set(m.m, 0);
	}

	public void ortho(float left, float right, float bottom, float top, float near, float far)
	{
		if (MathUtils.isEqual(left, right))
		{
			throw new IllegalArgumentException("left == right");
		}
		if (MathUtils.isEqual(bottom, top))
		{
			throw new IllegalArgumentException("bottom == top");
		}
		if (MathUtils.isEqual(near, far))
		{
			throw new IllegalArgumentException("near == far");
		}

		final float r_width = 1.0f / (right - left);
		final float r_height = 1.0f / (top - bottom);
		final float r_depth = 1.0f / (far - near);
		final float x = 2.0f * (r_width);
		final float y = 2.0f * (r_height);
		final float z = -2.0f * (r_depth);
		final float tx = -(right + left) * r_width;
		final float ty = -(top + bottom) * r_height;
		final float tz = -(far + near) * r_depth;

		m[0] = x;
		m[5] = y;
		m[10] = z;
		m[12] = tx;
		m[13] = ty;
		m[14] = tz;
		m[15] = 1.0f;
		m[1] = 0.0f;
		m[2] = 0.0f;
		m[3] = 0.0f;
		m[4] = 0.0f;
		m[6] = 0.0f;
		m[7] = 0.0f;
		m[8] = 0.0f;
		m[9] = 0.0f;
		m[11] = 0.0f;
	}

	public void frustum(float left, float right, float bottom, float top, float near, float far)
	{
		if (MathUtils.isEqual(left, right))
		{
			throw new IllegalArgumentException("left == right");
		}
		if (MathUtils.isEqual(bottom, top))
		{
			throw new IllegalArgumentException("bottom == top");
		}
		if (MathUtils.isEqual(near, far))
		{
			throw new IllegalArgumentException("near == far");
		}

		if (near <= 0.0f)
		{
			throw new IllegalArgumentException("near <= 0.0f");
		}
		if (far <= 0.0f)
		{
			throw new IllegalArgumentException("far <= 0.0f");
		}

		final float r_width = 1.0f / (right - left);
		final float r_height = 1.0f / (top - bottom);
		final float r_depth = 1.0f / (near - far);
		final float x = 2.0f * (near * r_width);
		final float y = 2.0f * (near * r_height);
		final float A = 2.0f * ((right + left) * r_width);
		final float B = (top + bottom) * r_height;
		final float C = (far + near) * r_depth;
		final float D = 2.0f * (far * near * r_depth);
		m[0] = x;
		m[5] = y;
		m[8] = A;
		m[9] = B;
		m[10] = C;
		m[14] = D;
		m[11] = -1.0f;
		m[1] = 0.0f;
		m[2] = 0.0f;
		m[3] = 0.0f;
		m[4] = 0.0f;
		m[6] = 0.0f;
		m[7] = 0.0f;
		m[12] = 0.0f;
		m[13] = 0.0f;
		m[15] = 0.0f;
	}

	/**
	 * Define a projection matrix in terms of a field of view angle, an aspect
	 * ratio, and z clip planes
	 * 
	 * @param fovy
	 *            field of view in y direction, in degrees
	 * @param aspect
	 *            width to height aspect ratio of the viewport
	 * @param zNear
	 * @param zFar
	 */
	public void perspective(float fovy, float aspect, float zNear, float zFar)
	{
		float f = 1.0f / (float) Math.tan(fovy * (Math.PI / 360.0));
		float rangeReciprocal = 1.0f / (zNear - zFar);

		m[0] = f / aspect;
		m[1] = 0.0f;
		m[2] = 0.0f;
		m[3] = 0.0f;

		m[4] = 0.0f;
		m[5] = f;
		m[6] = 0.0f;
		m[7] = 0.0f;

		m[8] = 0.0f;
		m[9] = 0.0f;
		m[10] = (zFar + zNear) * rangeReciprocal;
		m[11] = -1.0f;

		m[12] = 0.0f;
		m[13] = 0.0f;
		m[14] = 2.0f * zFar * zNear * rangeReciprocal;
		m[15] = 0.0f;
	}

	/**
	 * Scales matrix m in place by sx, sy, and sz
	 * 
	 * @param x
	 *            scale factor x
	 * @param y
	 *            scale factor y
	 * @param z
	 *            scale factor z
	 */
	public void scale(float x, float y, float z)
	{
		for (int i = 0; i < 4; i++)
		{
			int mi = i;
			m[mi] *= x;
			m[4 + mi] *= y;
			m[8 + mi] *= z;
		}
	}

	/**
	 * Translates matrix m by x, y, and z in place.
	 * 
	 * @param x
	 *            translation factor x
	 * @param y
	 *            translation factor y
	 * @param z
	 *            translation factor z
	 */
	public void translate(float x, float y, float z)
	{
		for (int i = 0; i < 4; i++)
		{
			int mi = i;
			m[12 + mi] += m[mi] * x + m[4 + mi] * y + m[8 + mi] * z;
		}
	}

	/**
	 * Rotates matrix m in place by angle a (in degrees) around the axis (x, y,
	 * z)
	 * 
	 * @param a
	 *            angle to rotate in degrees
	 * @param x
	 *            scale factor x
	 * @param y
	 *            scale factor y
	 * @param z
	 *            scale factor z
	 */
	public synchronized void rotate(float a, float x, float y, float z)
	{
		t1.setRotate(a, x, y, z);
		t2.multuply(this, t1);
		set(t2);
	}

	/**
	 * Rotates matrix m by angle a (in degrees) around the axis (x, y, z)
	 * 
	 * @param a
	 *            angle to rotate in degrees
	 * @param x
	 *            scale factor x
	 * @param y
	 *            scale factor y
	 * @param z
	 *            scale factor z
	 */
	public void setRotate(float a, float x, float y, float z)
	{
		m[3] = 0;
		m[7] = 0;
		m[11] = 0;
		m[12] = 0;
		m[13] = 0;
		m[14] = 0;
		m[15] = 1;
		a *= (float) (Math.PI / 180.0f);

		float s = (float) Math.sin(a);
		float c = (float) Math.cos(a);

		if (MathUtils.isEqual(x, 1) && MathUtils.isZero(y) && MathUtils.isZero(z))
		{
			m[5] = c;
			m[10] = c;
			m[6] = s;
			m[9] = -s;
			m[1] = 0;
			m[2] = 0;
			m[4] = 0;
			m[8] = 0;
			m[0] = 1;
		}
		else
			if (MathUtils.isZero(x) && MathUtils.isEqual(y, 1) && MathUtils.isZero(z))
			{
				m[0] = c;
				m[10] = c;
				m[8] = s;
				m[2] = -s;
				m[1] = 0;
				m[4] = 0;
				m[6] = 0;
				m[9] = 0;
				m[5] = 1;
			}
			else
				if (MathUtils.isZero(x) && MathUtils.isZero(y) && MathUtils.isEqual(z, 1))
				{
					m[0] = c;
					m[5] = c;
					m[1] = s;
					m[4] = -s;
					m[2] = 0;
					m[6] = 0;
					m[8] = 0;
					m[9] = 0;
					m[10] = 1;
				}
				else
				{
					float len = MathUtils.length(x, y, z);
					if (1.0f != len)
					{
						float recipLen = 1.0f / len;
						x *= recipLen;
						y *= recipLen;
						z *= recipLen;
					}
					float nc = 1.0f - c;
					float xy = x * y;
					float yz = y * z;
					float zx = z * x;
					float xs = x * s;
					float ys = y * s;
					float zs = z * s;
					m[0] = x * x * nc + c;
					m[4] = xy * nc - zs;
					m[8] = zx * nc + ys;
					m[1] = xy * nc + zs;
					m[5] = y * y * nc + c;
					m[9] = yz * nc - xs;
					m[2] = zx * nc - ys;
					m[6] = yz * nc + xs;
					m[10] = z * z * nc + c;
				}
	}

	/**
	 * Converts Euler angles to a rotation matrix
	 * 
	 * @param x
	 *            angle of rotation, in degrees
	 * @param y
	 *            angle of rotation, in degrees
	 * @param z
	 *            angle of rotation, in degrees
	 */
	public void setRotateEulerM(float x, float y, float z)
	{
		x *= (float) (Math.PI / 180.0f);
		y *= (float) (Math.PI / 180.0f);
		z *= (float) (Math.PI / 180.0f);
		float cx = (float) Math.cos(x);
		float sx = (float) Math.sin(x);
		float cy = (float) Math.cos(y);
		float sy = (float) Math.sin(y);
		float cz = (float) Math.cos(z);
		float sz = (float) Math.sin(z);
		float cxsy = cx * sy;
		float sxsy = sx * sy;

		m[0] = cy * cz;
		m[1] = -cy * sz;
		m[2] = sy;
		m[3] = 0.0f;

		m[4] = cxsy * cz + cx * sz;
		m[5] = -cxsy * sz + cx * cz;
		m[6] = -sx * cy;
		m[7] = 0.0f;

		m[8] = -sxsy * cz + sx * sz;
		m[9] = sxsy * sz + sx * cz;
		m[10] = cx * cy;
		m[11] = 0.0f;

		m[12] = 0.0f;
		m[13] = 0.0f;
		m[14] = 0.0f;
		m[15] = 1.0f;
	}

	/**
	 * Define a viewing transformation in terms of an eye point, a center of
	 * view, and an up vector.
	 * 
	 * @param rm
	 *            returns the result
	 * @param rmOffset
	 *            index into rm where the result matrix starts
	 * @param eyeX
	 *            eye point X
	 * @param eyeY
	 *            eye point Y
	 * @param eyeZ
	 *            eye point Z
	 * @param centerX
	 *            center of view X
	 * @param centerY
	 *            center of view Y
	 * @param centerZ
	 *            center of view Z
	 * @param upX
	 *            up vector X
	 * @param upY
	 *            up vector Y
	 * @param upZ
	 *            up vector Z
	 */
	public void setLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX,
			float upY, float upZ)
	{
		// See the OpenGL GLUT documentation for gluLookAt for a description
		// of the algorithm. We implement it in a straightforward way:

		float fx = centerX - eyeX;
		float fy = centerY - eyeY;
		float fz = centerZ - eyeZ;

		// Normalize f
		float rlf = 1.0f / MathUtils.length(fx, fy, fz);
		fx *= rlf;
		fy *= rlf;
		fz *= rlf;

		// compute s = f x up (x means "cross product")
		float sx = fy * upZ - fz * upY;
		float sy = fz * upX - fx * upZ;
		float sz = fx * upY - fy * upX;

		// and normalize s
		float rls = 1.0f / MathUtils.length(sx, sy, sz);
		sx *= rls;
		sy *= rls;
		sz *= rls;

		// compute u = s x f
		float ux = sy * fz - sz * fy;
		float uy = sz * fx - sx * fz;
		float uz = sx * fy - sy * fx;

		m[0] = sx;
		m[1] = ux;
		m[2] = -fx;
		m[3] = 0.0f;

		m[4] = sy;
		m[5] = uy;
		m[6] = -fy;
		m[7] = 0.0f;

		m[8] = sz;
		m[9] = uz;
		m[10] = -fz;
		m[11] = 0.0f;

		m[12] = 0.0f;
		m[13] = 0.0f;
		m[14] = 0.0f;
		m[15] = 1.0f;

		translate(eyeX, eyeY, eyeZ);
	}

	public void setLookAt(Vector3D eye, Vector3D center, Vector3D up)
	{
		setLookAt(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
	}

	private final void swap(int a, int b)
	{
		final float t = m[a];
		m[a] = m[b];
		m[b] = t;
	}
}
