package js.gles11.tools;

import javax.microedition.khronos.opengles.GL10;

public class Lighting
{
	private static float[] values = new float[] { 0, 0, 0, 1 };

	public static void on(GL10 gl)
	{
		gl.glEnable(GL10.GL_LIGHTING);
	}

	public static void off(GL10 gl)
	{
		gl.glDisable(GL10.GL_LIGHTING);
	}

	public static void setModelAmbient(GL10 gl, float r, float g, float b, float a)
	{
		values[0] = r;
		values[1] = g;
		values[2] = b;
		values[3] = a;

		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, values, 0);
	}

}
