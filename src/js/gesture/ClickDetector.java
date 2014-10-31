package js.gesture;


import js.math.MathUtils;
import android.os.SystemClock;
import android.view.MotionEvent;

public class ClickDetector
{
	private float startX;
	private float startY;
	private long startTime = 0;
	private int clickCount = 0;

	private float diameterQuad;
	private int detectCount;
	private int detectTime;

	private ClickDetectorListener clickDetectorListener;

	private boolean isOutOfRange(float x, float y)
	{
		return MathUtils.quad(x - startX, y - startY) > diameterQuad;
	}

	public ClickDetector(ClickDetectorListener clickDetectorListener, int detectCount, int detectTime, float radius)
	{
		if (clickDetectorListener == null)
		{
			throw new NullPointerException("clickDetectorListener == null");
		}
		this.clickDetectorListener = clickDetectorListener;
		this.detectCount = detectCount;
		this.detectTime = detectTime;
		this.diameterQuad = (radius * 0.5f) * (radius * 0.5f);
	}

	public ClickDetector(ClickDetectorListener clickDetectorListener, int detectCount, int detectTime)
	{
		this(clickDetectorListener, detectCount, detectTime, 100);
	}

	public ClickDetector(ClickDetectorListener clickDetectorListener, int detectCount)
	{
		this(clickDetectorListener, detectCount, 1000);
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		boolean result = false;
		
		if (event.getPointerCount() == 1)
		{
			int action = event.getAction();
			float x = event.getX();
			float y = event.getY();
			switch (action)
			{
				case MotionEvent.ACTION_DOWN:
					if (clickCount == 0)
					{
						startX = x;
						startY = y;
						startTime = SystemClock.elapsedRealtime();
						clickCount++;
					}
					else
					{
						clickCount = isOutOfRange(x, y) ? 0 : clickCount + 1;
					}
					break;
				case MotionEvent.ACTION_UP:
					if (clickCount > 0)
					{
						if (isOutOfRange(x, y))
						{
							clickCount = 0;
						}
						else
						{
							if (clickCount == detectCount && SystemClock.elapsedRealtime() - startTime <= detectTime)
							{
								clickCount = 0;
								clickDetectorListener.onClickDetected(startX, startY);
								result = true;
							}
						}
					}
					break;
			}
		}
		else
		{
			clickCount = 0;
		}
		return result;
	}

	public interface ClickDetectorListener
	{
		void onClickDetected(float x, float y);
	}
}
