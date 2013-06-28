package js.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.util.Log;

public class QuadBuffer
{
	private static String TAG = "QuadBuffer";

	public FloatBuffer buffer = null;

	public QuadBuffer()
	{
		buffer = generate(0, 0, 1, 1);
	}

	public QuadBuffer(float[] values)
	{
		buffer = generate(values);
	}

	public QuadBuffer(float x1, float y1, float x2, float y2)
	{
		buffer = generate(x1, y1, x2, y2);
	}

	public static FloatBuffer generate(float[] coords)
	{
		if (coords.length != 8)
		{
			Log.e(TAG, "coorlds[].length != 8");
			return generate(0, 0, 1, 1);
		}
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * coords.length);

		byteBuffer.order(ByteOrder.nativeOrder());

		FloatBuffer buffer = byteBuffer.asFloatBuffer();

		buffer.put(coords);

		buffer.position(0);

		return buffer;
	}

	public static FloatBuffer generate(float x1, float y1, float x2, float y2)
	{
		return generate(new float[] { x1, y1, x2, y1, x2, y2, x1, y2 });
	}

	public static void update(FloatBuffer buffer, float x1, float y1, float x2, float y2)
	{
		buffer.put(x1);
		buffer.put(y1);
		buffer.put(x2);
		buffer.put(y1);
		buffer.put(x2);
		buffer.put(y2);
		buffer.put(x1);
		buffer.put(y2);
		buffer.position(0);
	}

	public void update(float x1, float y1, float x2, float y2)
	{
		update(buffer, x1, y1, x2, y2);
	}
}
