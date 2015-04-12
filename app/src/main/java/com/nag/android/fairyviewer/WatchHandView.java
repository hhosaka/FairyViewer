package com.nag.android.fairyviewer;

import java.util.Random;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.widget.ImageView;

abstract class WatchHandView extends ImageView {

	private float current = 0.0f;
	private Random rand = new Random();
	public WatchHandView(Context context) {
		super(context);
	}

	public WatchHandView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WatchHandView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	protected abstract float getAngle();

	public void random(int duration){
		move(duration, rand.nextFloat()*720-360.0f);
	}

	public void adjust(int duration){
		move(duration, getAngle());
	}

	private void move(int duration, float next) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this
				, PropertyValuesHolder.ofFloat("rotation", current, next));
		objectAnimator.setDuration(duration);
		current = next%360;
		objectAnimator.start();
	}
}
