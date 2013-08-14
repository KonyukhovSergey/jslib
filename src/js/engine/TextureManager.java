package js.engine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLUtils;
import android.os.Parcelable.Creator;
import android.util.Log;

public class TextureManager
{
	private static final String TAG = "js.engine.TextureManager";

	private int[] indexes = new int[1];
	private List<FloatBuffer> coordsList = new ArrayList<FloatBuffer>();

	public int createTexture(GL10 gl, Bitmap bitmap, boolean isRecycleBitmap)
	{
		gl.glGenTextures(1, indexes, 0);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, indexes[0]);

		Log.v(TAG, String.format("generated texure name = %d", indexes[0]));

		gl.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, 1);

		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, 0);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, 0);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		Log.v(TAG, String.format("texure name = %d is uploaded", indexes[0]));

		if (isRecycleBitmap)
		{
			Log.v(TAG, String.format("bitmap %s w = %d h = %d will be recycled", "id = " + bitmap, bitmap.getWidth(),
					bitmap.getHeight()));
			bitmap.recycle();
		}
		return indexes[0];
	}

	public int createTexture(Context context, GL10 gl, String assetBitmapName)
	{
		return createTexture(gl, load(context, assetBitmapName), true);
	}

	public int createCoords(float[] coords)
	{
		coordsList.add(RectangleBuffer.create(coords));
		return coordsList.size() - 1;
	}

	public int createCoords(float x1, float y1, float x2, float y2)
	{
		return createCoords(new float[] { x1, y1, x2, y1, x2, y2, x1, y2 });
	}

	public int createCoords(int left, int top, int width, int height, int textureWidth, int textureHeight)
	{
		return createCoords((float) left / (float) textureWidth, (float) top / (float) textureHeight,
				((float) (left + width)) / (float) textureWidth, ((float) (top + height)) / (float) textureHeight);
	}

	public void useTexture(GL10 gl, int textureName)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
	}

	public void useCoords(GL10 gl, int coordsName)
	{
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordsList.get(coordsName));
	}

	public FloatBuffer getCoordsBuffer(int coordsname)
	{
		FloatBuffer buf = coordsList.get(coordsname);
		buf.position(0);
		return buf;
	}

	public void deleteTexure(GL10 gl, int textureName)
	{
		indexes[0] = textureName;
		gl.glDeleteTextures(1, indexes, 0);
	}

	public static Bitmap noise(int size, float freq, float t)
	{
		int colors[] = new int[size * size];

		for (int i = 0; i < colors.length; i++)
		{
			float x = ((float) (i % size)) / freq;
			float y = ((float) (i / size)) / freq;

			int r = (int) (128.0f * (1.0f + SimplexNoise.noise(x, y, t)));
			int g = (int) (128.0f * (1.0f + SimplexNoise.noise(x + size, y, t)));
			int b = (int) (128.0f * (1.0f + SimplexNoise.noise(x, y + size, t)));

			colors[i] = (0xff << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
		}

		return Bitmap.createBitmap(colors, size, size, Config.ARGB_8888);
	}

	public static Bitmap load(Context context, String name)
	{
		try
		{
			InputStream is;
			is = context.getAssets().open(name);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.v(TAG,
					String.format("bitmap %s w = %d h = %d is loaded", "id = " + bitmap, bitmap.getWidth(),
							bitmap.getHeight()));

			return bitmap;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
