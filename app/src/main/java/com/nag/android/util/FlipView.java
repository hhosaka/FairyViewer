package com.nag.android.util;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FlipView extends ImageView {

	private final int INTERVAL_DEFAULT = 500;
	private final String ATTR_IMAGES = "images";
	private final String ATTR_INTERVAL = "interval";
	private Drawable[] drawables;
	private int current=0;
	private boolean loop = true;
	private Timer timer = null;
	private Handler handler = new Handler();
    private Matrix matrix = new Matrix();
    private AngleMeter meter;
    private FlipViewListener listener = null;

	public interface FlipViewListener{
		void onFinish();
	}

    public void setAngleMeter(AngleMeter meter){
        this.meter = meter;
    }

	public FlipView(Context context, AttributeSet attrs) {
		this(context, attrs, null);
	}

	public FlipView(Context context, AttributeSet attrs, FlipViewListener listener) {
		super(context, attrs);
        setScaleType(ScaleType.MATRIX);
		if (attrs!=null){
			if(!isInEditMode()){
				int id = attrs.getAttributeResourceValue(null, ATTR_IMAGES, 0);
				if(id!=0){
					setSource(id,
							attrs.getAttributeIntValue(null, ATTR_INTERVAL, INTERVAL_DEFAULT),
							loop, listener);
				}
			}
		}
	}

	public void reset(){
		stop();
		setImageDrawable(null);
	}

	public void setSource(int id, int interval, boolean loop) {
		setSource(id, interval, loop, null);
	}

	public void setSource(int id, int interval, boolean loop, FlipViewListener listener){
        stop();
		this.loop = loop;
		this.listener = listener;
		current = 0;
		TypedArray array = getResources().obtainTypedArray(id);
		drawables = new Drawable[array.length()];
		for(int i=0; i<array.length(); ++i){
			drawables[i] = array.getDrawable(i);
		}
		array.recycle();
		start(interval);
	}

	private void start(int interval){
		current=0;
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				if (current < drawables.length - 1) {
                    postDraw();
                }else if(loop){
					current = 0;
                    postDraw();
				}else {
                    stop();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onFinish();
                            }
                        }
                    });
				}
			}}, 0, interval);
	}

    private void postDraw() {
        handler.post(new Runnable(){
            @Override
            public void run() {
                Drawable drawable = drawables[current++];
                if(meter!=null) {
                    matrix.reset();
                    matrix.postRotate(meter.getAngle(), drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
                    setImageMatrix(matrix);
                }
				setScaleType(ScaleType.FIT_CENTER);
                setImageDrawable(drawable);
            }});
    }

    public void stop(){
		loop = false;
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
	}
}
