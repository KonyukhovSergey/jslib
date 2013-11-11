package js.engine;

import javax.microedition.khronos.opengles.GL10;

public class Light
{

	public static void init(GL10 gl, int light)
	{
		float[] values = new float[] { 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1 };

		gl.glLightfv(light, GL10.GL_POSITION, values, 0);
		gl.glLightfv(light, GL10.GL_AMBIENT, values, 4);
		gl.glLightfv(light, GL10.GL_DIFFUSE, values, 8);
		gl.glLightfv(light, GL10.GL_SPECULAR, values, 12);

		gl.glLightf(light, GL10.GL_CONSTANT_ATTENUATION, 1.0f);
		gl.glLightf(light, GL10.GL_LINEAR_ATTENUATION, 0.0f);
		gl.glLightf(light, GL10.GL_QUADRATIC_ATTENUATION, 0.0f);
	}

	public static void setPosition(GL10 gl,int light, float x, float y, float z)
	{
		float[] values = new float[] { x, y, z, 1 };
		gl.glLightfv(light, GL10.GL_POSITION, values, 0);
	}
}
