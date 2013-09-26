package js.engine;

import javax.microedition.khronos.opengles.GL10;

public class Light
{
	private static boolean[] isUsed = null;
	private GL10 gl;

	public Light(GL10 gl)
	{
		this.gl = gl;
		init();
		
		gl.glLightfv(light, pname, params, offset);
	}

	private void init()
	{
		if (isUsed != null)
		{
			int buff[] = new int[1];
			gl.glGetIntegerv(gl.GL_MAX_LIGHTS, buff, 0);
			isUsed = new boolean[buff[0]];
		}

	}

}
