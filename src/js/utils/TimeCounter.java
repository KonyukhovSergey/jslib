package js.utils;

import android.os.SystemClock;

public class TimeCounter
{
	private long startTime = SystemClock.elapsedRealtime();
	private float periodMultipler = 1;

	public void start()
	{
		startTime = SystemClock.elapsedRealtime();
	}

	public float elapsed()
	{
		return ((float) (SystemClock.elapsedRealtime() - startTime)) * 0.001f;
	}

	public void calculate(int milliseconds)
	{
		start();
		periodMultipler = milliseconds < 1 ? 1.0f : 1.0f / ((float) milliseconds);
	}

	public float position()
	{
		return elapsed() * periodMultipler;
	}

	public float delta()
	{
		float rValue = elapsed();
		start();
		return rValue;
	}

	public int ms()
	{
		return (int) (SystemClock.elapsedRealtime() - startTime);
	}
}
