package com.nag.android.fairyviewerengine;

import java.util.Random;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;

public abstract class WatchHandView extends ImageView {

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

	public void random(int duration) {
		random(duration, null);
	}

	public void random(int duration, Animator.AnimatorListener listener){
		move(duration, rand.nextFloat()*720-360.0f, listener);
	}

	public void setToNow(int duration) {
		setToNow(duration, null);
	}
	public void setToNow(int duration, Animator.AnimatorListener listener){
		move(duration, getAngle(),listener);
	}

	private float arrange(float current, float next){
		if(current-next<180 && current-next>-180){
			return next+360f;
		}
		return next;
	}

	private void move(int duration, float next, Animator.AnimatorListener listener) {
		//Log.d("H:", "H:current" + current + " next=" + next);
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this
				, PropertyValuesHolder.ofFloat("rotation", current, arrange(current, next)));
		objectAnimator.setDuration(duration);
		if(listener!=null)objectAnimator.addListener(listener);
		current = next%360;
		objectAnimator.start();
	}
}
