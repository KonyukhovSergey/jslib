package js.math;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.util.FloatMath;

public class Basis
{
	public float px = 0, py = 0, pz = 0;
	public float fx = 0, fy = 0, fz = 1;
	public float ux = 0, uy = 1, uz = 0;
	public float rx = 1, ry = 0, rz = 0;

	public void moveTo(float x, float y, float z)
	{
		px = x;
		py = y;
		pz = z;
	}

	public void move(float lr, float ud, float fb)
	{
		px += fx * fb + rx * lr + ux * ud;
		py += fy * fb + ry * lr + uy * ud;
		pz += fz * fb + rz * lr + uz * ud;
	}

	public void normalize()
	{
		float d;
		d = sqrt(fx * fx + fy * fy + fz * fz);
		fx /= d;
		fy /= d;
		fz /= d;
		d = sqrt(ux * ux + uy * uy + uz * uz);
		ux /= d;
		uy /= d;
		uz /= d;
		d = sqrt(rx * rx + ry * ry + rz * rz);
		rx /= d;
		ry /= d;
		rz /= d;
	}

	public void moveAndRotate(float lr, float ud, float fb, float alr, float aud, float acw)
	{
		px += fx * fb + rx * lr + ux * ud;
		py += fy * fb + ry * lr + uy * ud;
		pz += fz * fb + rz * lr + uz * ud;

		float tx, ty, tz;
		float sint, cost;

		sint = sin(-acw);
		cost = cos(-acw);

		// t=cost*r+sint*u (r`=r; acw)
		tx = cost * rx + sint * ux;
		ty = cost * ry + sint * uy;
		tz = cost * rz + sint * uz;

		// u=cost*u-sint*r
		ux = cost * ux - sint * rx;
		uy = cost * uy - sint * ry;
		uz = cost * uz - sint * rz;

		rx = tx;
		ry = ty;
		rz = tz;

		sint = sin(-aud);
		cost = cos(-aud);

		// t=cost*f+sint*u (f`=t; aud)
		tx = cost * fx + sint * ux;
		ty = cost * fy + sint * uy;
		tz = cost * fz + sint * uz;

		// u=cost*u-sint*f
		ux = cost * ux - sint * fx;
		uy = cost * uy - sint * fy;
		uz = cost * uz - sint * fz;

		fx = tx;
		fy = ty;
		fz = tz;

		sint = sin(alr);
		cost = cos(alr);

		// t=cost*r+sint*f (r`=t; alr)
		tx = cost * rx + sint * fx;
		ty = cost * ry + sint * fy;
		tz = cost * rz + sint * fz;

		// f=cost*f-sint*r
		fx = cost * fx - sint * rx;
		fy = cost * fy - sint * ry;
		fz = cost * fz - sint * rz;

		rx = tx;
		ry = ty;
		rz = tz;
		// end
		normalize();
	}

	public void lookAt(float x, float y, float z)
	{
		fx = px - x;
		fy = py - y;
		fz = pz - z;
		normalize();
	}

	public void rotateLR(float angle)
	{
		// �������� ������������ �������
		float tx, ty, tz;
		float sint, cost;

		sint = sin(angle);
		cost = cos(angle);

		// t=cost*r+sint*f (r`=t; alr)
		tx = cost * rx + sint * fx;
		ty = cost * ry + sint * fy;
		tz = cost * rz + sint * fz;

		// f=cost*f-sint*r
		fx = cost * fx - sint * rx;
		fy = cost * fy - sint * ry;
		fz = cost * fz - sint * rz;

		rx = tx;
		ry = ty;
		rz = tz;
	}

	public void rotateUD(float angle)
	{
		// ������ �������� ������������ �������
		float tx, ty, tz;
		float sint, cost;

		sint = sin(-angle);
		cost = cos(-angle);

		// t=cost*f+sint*u (f`=t; aud)
		tx = cost * fx + sint * ux;
		ty = cost * fy + sint * uy;
		tz = cost * fz + sint * uz;

		// u=cost*u-sint*f
		ux = cost * ux - sint * fx;
		uy = cost * uy - sint * fy;
		uz = cost * uz - sint * fz;

		fx = tx;
		fy = ty;
		fz = tz;
		// end
	}

	public void rotateCW(float angle)
	{
		// ������ �������� ������������ �������
		float tx, ty, tz;
		float sint, cost;

		sint = sin(-angle);
		cost = cos(-angle);

		// t=cost*r+sint*u (r`=r; acw)
		tx = cost * rx + sint * ux;
		ty = cost * ry + sint * uy;
		tz = cost * rz + sint * uz;

		// u=cost*u-sint*r
		ux = cost * ux - sint * rx;
		uy = cost * uy - sint * ry;
		uz = cost * uz - sint * rz;

		rx = tx;
		ry = ty;
		rz = tz;
		// end
	}

	public void setCamera(GL10 gl)
	{
		GLU.gluLookAt(gl, px, py, pz, px + fx, py + fy, pz + fz, ux, uy, uz);
	}

	public void transform(GL10 gl)
	{
		float tfx, tfy, tfz, tux, tuy, tuz;
		float a, sina, cosa;

		gl.glTranslatef(px, py, pz);

		tfx = fx;
		tfy = fy;
		tfz = fz;
		tux = ux;
		tuy = uy;
		tuz = uz;

		a = +atan2(fy, fz);
		gl.glRotatef(-a * 180.0f / 3.141592f, 1, 0, 0);

		cosa = cos(a);
		sina = sin(a);

		tfz = fz * cosa + fy * sina;
		tfy = fz * sina - fy * cosa;

		tuz = uz * cosa + uy * sina;
		tuy = uz * sina - uy * cosa;

		a = -atan2(tfx, tfz);
		gl.glRotatef(-a * 180.0f / 3.141592f, 0, 1, 0);

		sina = sin(a);
		cosa = cos(a);

		tfx = tux * cosa + tuz * sina;
		// tfz=tux*sina-tuz*cosa;

		a = -atan2(tfx, tuy);
		gl.glRotatef(-a * 180.0f / 3.141592f, 0, 0, 1);
	}

	public void rotate(float alr, float aud, float acw)
	{
		float tx, ty, tz;
		float sint, cost;

		sint = sin(-acw);
		cost = cos(-acw);

		// t=cost*r+sint*u (r`=r; acw)
		tx = cost * rx + sint * ux;
		ty = cost * ry + sint * uy;
		tz = cost * rz + sint * uz;

		// u=cost*u-sint*r
		ux = cost * ux - sint * rx;
		uy = cost * uy - sint * ry;
		uz = cost * uz - sint * rz;

		rx = tx;
		ry = ty;
		rz = tz;

		sint = sin(-aud);
		cost = cos(-aud);

		// t=cost*f+sint*u (f`=t; aud)
		tx = cost * fx + sint * ux;
		ty = cost * fy + sint * uy;
		tz = cost * fz + sint * uz;

		// u=cost*u-sint*f
		ux = cost * ux - sint * fx;
		uy = cost * uy - sint * fy;
		uz = cost * uz - sint * fz;

		fx = tx;
		fy = ty;
		fz = tz;

		sint = sin(alr);
		cost = cos(alr);

		// t=cost*r+sint*f (r`=t; alr)
		tx = cost * rx + sint * fx;
		ty = cost * ry + sint * fy;
		tz = cost * rz + sint * fz;

		// f=cost*f-sint*r
		fx = cost * fx - sint * rx;
		fy = cost * fy - sint * ry;
		fz = cost * fz - sint * rz;

		rx = tx;
		ry = ty;
		rz = tz;
		// end
		normalize();
	}

	private static final float cos(float angle)
	{
		return FloatMath.cos(angle);
	}

	private static final float sin(float angle)
	{
		return FloatMath.sin(angle);
	}

	private static final float sqrt(float value)
	{
		return FloatMath.sqrt(value);
	}

	private static final float atan2(float x, float y)
	{
		return (float) Math.atan2(x, y);
	}
}
