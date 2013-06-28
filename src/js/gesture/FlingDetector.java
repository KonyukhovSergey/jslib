package js.gesture;

import android.os.SystemClock;
import android.view.MotionEvent;

public class FlingDetector
{
	private static final int MAX_POINTER_COUNT = 21;

	private final float[] lastX = new float[MAX_POINTER_COUNT];
	private final float[] lastY = new float[MAX_POINTER_COUNT];
	private final long[] lastTime = new long[MAX_POINTER_COUNT];

	private FlingDetectorListener flingDetectorListener;

	public FlingDetector(FlingDetectorListener flingDetectorListener)
	{
		if (flingDetectorListener == null)
		{
			throw new NullPointerException("flingDetectorListener == null");
		}
		
		this.flingDetectorListener = flingDetectorListener;	
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		boolean result = false;

		int action = event.getActionMasked();
		int index = event.getActionIndex();
		int id = event.getPointerId(index);
		float x = event.getX(index);
		float y = event.getY(index);

		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
			{
				lastX[id] = x;
				lastY[id] = y;
				lastTime[id] = SystemClock.elapsedRealtime();
			}
				break;

			case MotionEvent.ACTION_MOVE:
			{
				int pointerCount = event.getPointerCount();
				long currentTime = SystemClock.elapsedRealtime();
				
				for (int i = 0; i < pointerCount; i++)
				{
					int currentID = event.getPointerId(i);
					float currentX = event.getX(i);
					float currentY = event.getY(i);

					flingDetectorListener.onFling(lastX[currentID], lastY[currentID], currentX, currentY,
							(int) (currentTime - lastTime[currentID]));

					lastX[currentID] = currentX;
					lastY[currentID] = currentY;
					lastTime[currentID] = currentTime;
					
					result = true;
				}
			}
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			{
				flingDetectorListener.onFling(lastX[id], lastY[id], x, y,
						(int) (SystemClock.elapsedRealtime() - lastTime[id]));
				
				result = true;
			}
				break;
		}

		return result;
	}

	public interface FlingDetectorListener
	{
		void onFling(float fromX, float fromY, float toX, float toY, int deltaTime);
	}
}
