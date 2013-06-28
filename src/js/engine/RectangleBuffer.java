package js.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.util.Log;

public class RectangleBuffer
{
	private static String TAG = "js.engine.RectangleBuffer";
	private FloatBuffer buffer = null;

	public RectangleBuffer()
	{
		buffer = create(0, 0, 1, 1);
	}

	public RectangleBuffer(float[] values)
	{
		buffer = create(values);
	}

	public RectangleBuffer(float x1, float y1, float x2, float y2)
	{
		buffer = create(x1, y1, x2, y2);
	}
	
	public void update(float x1, float y1, float x2, float y2)
	{
		update(buffer, x1, y1, x2, y2);
	}
	
	public FloatBuffer buffer()
	{
		return buffer;
	}

	public static FloatBuffer create(float[] coords)
	{
		if (coords.length != 8)
		{
			Log.e(TAG, "coorlds[].length != 8");
			return create(0, 0, 1, 1);
		}
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * coords.length);

		byteBuffer.order(ByteOrder.nativeOrder());

		FloatBuffer buffer = byteBuffer.asFloatBuffer();

		buffer.put(coords);

		buffer.position(0);

		return buffer;
	}

	public static FloatBuffer create(float x1, float y1, float x2, float y2)
	{
		return create(new float[] { x1, y1, x2, y1, x2, y2, x1, y2 });
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
}
