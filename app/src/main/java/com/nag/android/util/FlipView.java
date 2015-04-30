package com.nag.android.util;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nag.android.fairyviewer.ShakeManager;

public class FlipView extends ImageView {

	private final int INTERVAL_DEFAULT = 500;
	private final String ATTR_IMAGES = "images";
	private final String ATTR_INTERVAL = "interval";
	private Drawable[] drawables;
	private int current=0;
	private boolean loop = true;
	private Timer timer = null;
	private Handler handler = new Handler();

	public interface FlipViewListener{
		void onFinish();
	}

	private FlipViewListener listener = null;

	public FlipView(Context context, AttributeSet attrs) {
		super(context, attrs);
        setScaleType(ScaleType.MATRIX);
		if (attrs!=null){
			if(!isInEditMode()){
				int id = attrs.getAttributeResourceValue(null, ATTR_IMAGES, 0);
				if(id!=0){
					setSource(id,
							attrs.getAttributeIntValue(null, ATTR_INTERVAL, INTERVAL_DEFAULT),
							loop);
				}
			}
		}
	}

	public void setOnFlipViewListener(FlipViewListener listener){
		this.listener = listener;
	}

	public void reset(){
		stop();
		setImageDrawable(null);
	}

	public void setSource(int id, int interval, boolean loop){
        stop();
		this.loop = loop;
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
//		setImageDrawable(drawables[0]);
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				if (current>=drawables.length) {
					if(loop){
						current = 0;
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
				}
				handler.post(new Runnable(){
					@Override
					public void run() {
						invalidate();
					}});
			}}, 0, interval);
	}
	
	public void stop(){
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
	}

    public void setAngleMeter(AngleMeter meter){
        this.meter = meter;
    }

    private Matrix matrix = new Matrix();
    private AngleMeter meter;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(drawables!=null){
            if(meter!=null) {
                matrix.reset();
                matrix.postRotate(meter.getAngle(), canvas.getWidth() / 2, canvas.getHeight() / 2);
                setImageMatrix(matrix);
            }
			setImageDrawable(drawables[current++]);
		}
	}
}
