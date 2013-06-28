package js.utils;

import android.content.Intent;

public class IntentsCollection
{
	public static Intent pickAnImage()
	{
		return new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	}
}
