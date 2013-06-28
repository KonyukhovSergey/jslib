package js.engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import js.engine.FrameRateCalculator.FrameRateUpdateInterface;
import js.utils.TimeCounter;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 
 * @author jauseg
 *
 */
public abstract class BaseEngine extends GLSurfaceView implements FrameRateUpdateInterface, Renderer
{
	private static final String TAG = "js.engine.BageEngine";
	private final TimeCounter timeCounter = new TimeCounter();

	private FrameRateCalculator fps = null;

	public BaseEngine(Context context)
	{
		super(context);

		fps = new FrameRateCalculator(this);
	
		timeCounter.start();
		onLoad(context);
		Log.v(TAG, String.format("assets loading time: %d ms", timeCounter.ms()));

		setEGLContextClientVersion(1);
		setRenderer(this);
		setRenderMode(RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public void onFrameRateUpdate(FrameRateCalculator fps)
	{
		//Log.v(TAG, fps.frameString());
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		Log.v(TAG, "onSurfaceCreated");
		onInit(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		Log.v(TAG, String.format("onSurfaceChanged: w = %d, h = %d", width, height));
		onSize(gl, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		fps.frameBegin();
		onDraw(gl);
		fps.frameDone();
	}
	
	public abstract boolean onTouchEvent(MotionEvent event);
	public abstract void onLoad(Context context);
	public abstract void onInit(GL10 gl);
	public abstract void onSize(GL10 gl, int width, int height);
	public abstract void onDraw(GL10 gl);
}
