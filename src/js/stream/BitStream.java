package js.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitStream
{
	public static final void u128to(OutputStream stream, int value) throws IOException
	{
		do
		{
			int oneByte = value & 0x7f;
			
			value = value >> 7;

			if (value > 0)
			{
				oneByte = oneByte | 0x80;
			}

			stream.write(oneByte);
		}
		while (value > 0);
	}

	public static final int u128from(InputStream stream) throws IOException
	{
		int value = 0;
		int shift = 0;

		while (shift < 32)
		{
			int oneByte = stream.read();

			value |= ((oneByte & 0x7f) << shift);

			if ((oneByte & 0x80) == 0)
			{
				break;
			}

			shift += 7;
		}

		return value;
	}

	public static final void stringTo(OutputStream stream, String value) throws IOException
	{
		if (value == null)
		{
			value = "";
		}

		byte[] data = value.getBytes();

		u128to(stream, data.length);
		stream.write(data);
	}

	public static final String stringFrom(InputStream stream) throws IOException
	{
		int length = u128from(stream);

		byte[] data = new byte[length];

		stream.read(data);

		return new String(data);
	}
}
