package js.utils;

import java.io.IOException;
import java.io.OutputStream;

public class FileUtils
{
	public static void Write(OutputStream stream, String value) throws IOException
	{
		byte[] data = value.getBytes();
		stream.write(data.length);
		stream.write(data.length >> 8);
	}
}
