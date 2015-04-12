package com.nag.android.util;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class AnimationView extends View {

	private final int INTERVAL_DEFAULT = 500;
	private final String ATTR_SOURCE = "source";
	private final String ATTR_INTERVAL = "interval";
	private Bitmap source;
	private int size;
	private int current=0;
	private Timer timer = null;
	private Handler handler = new Handler();

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (attrs!=null){
			if(!isInEditMode()){
				setImageResources(attrs.getAttributeResourceValue(null, ATTR_SOURCE, 0));
				start(attrs.getAttributeIntValue(null, ATTR_INTERVAL, INTERVAL_DEFAULT));
			}
		}
	}

	public void setImageResources(int id){
		assert(id!=0);// TODO
		source =BitmapFactory.decodeResource(getResources(), id);

//		source = ((BitmapDrawable)getResources().getDrawable(id)).getBitmap();
		size = source.getHeight();
		int width = source.getWidth();
	}

	private void start(int period){
		current=0;
//		this.setImageBitmap(source);
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				if (++current>=source.getWidth()/size) {
					current = 0;
				}
				handler.post(new Runnable(){
					@Override
					public void run() {
						invalidate();
					}});
			}}, 0, period);
	}
	
	public void stop(){
		timer.cancel();
		timer = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(source!=null){
			Rect src = new Rect(size*current, 0, size*(current+1),size);
			Rect dst = new Rect(0, 0, size,size);
			canvas.drawBitmap(source, src, dst, null);
		}
	}
}
