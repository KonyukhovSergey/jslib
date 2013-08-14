package js.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class BufferAllocator
{
	public static FloatBuffer createFloatBuffer(int size)
	{
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * size);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = byteBuffer.asFloatBuffer();
		buffer.position(0);
		return buffer;
	}
	
	public static ByteBuffer createBuffer(int size)
	{
		ByteBuffer buf = ByteBuffer.allocateDirect(size);
		buf.order(ByteOrder.nativeOrder());
		return buf;
	}
}
