package com.nag.android.fairyviewer;

import android.animation.Animator;
import android.content.Context;

import com.nag.android.fairyviewerengine.Fairy;
import com.nag.android.fairyviewerengine.Watch;
import com.nag.android.util.FlipView;

import java.util.Random;

class FairyShadowGuy implements Fairy
{
	private static final int INTERVAL = 100;
	private static final int DURATION=6000;
	private boolean onaction = false;
	private Watch watch;

	@Override
	public void onShake(Context context, Watch watch) {
		if(!onaction) {
			watch.getFairyHandView().reset();
			watch.getHourHandView().random(DURATION);
			watch.getMinuteHandView().random(DURATION);
		}
	}
	private interface Source {
		int getSourceEnter();
		int getSourceWindUp();
		int getSourceBow();
		int getSourceExit();
	}
	private Source source;
	private static class Left implements Source {
		public int getSourceEnter(){return R.array.fairy_enter_r;}
		public int getSourceWindUp(){
			return R.array.fairy_windup_r;
		}
		public int getSourceBow(){
			return R.array.fairy_bow_r;
		}
		public int getSourceExit(){
			return R.array.fairy_exit_r;
		}
	}
	private static class Right implements Source {
		public int getSourceEnter() {return R.array.fairy_enter;}
		public int getSourceWindUp() {	return R.array.fairy_windup;}
		public int getSourceBow() {
			return R.array.fairy_bow;
		}
		public int getSourceExit() {
			return R.array.fairy_exit;
		}
	}

	private static Random rand = new Random();

	public static Source getInstance(){
		switch(rand.nextInt(2)){
			case 0:
				return new Left();
			case 1:
				return new Right();
			default:
				throw new UnsupportedOperationException();
		}
	}

	@Override
	public void onTap(Context context, Watch watch){
		if(!onaction) {
			onaction = true;
			source = getInstance();
			this.watch = watch;
			watch.getFairyHandView().setSource(source.getSourceEnter(), INTERVAL, false, new Step2());
		}
	}

	@Override
	public void onRolling(Context context, Watch watch){
		//do nothing
	}

	class Step2 implements FlipView.FlipViewListener {
		@Override
		public void onFinish() {
//			shakemanager.setAngle();
			watch.getFairyHandView().setSource(source.getSourceWindUp(), INTERVAL, true);
			watch.getHourHandView().setToNow(DURATION);
			watch.getMinuteHandView().setToNow(DURATION, new Step3());
		}
	}

	class Step3 implements Animator.AnimatorListener{
		@Override
		public void onAnimationStart(Animator animator) {
			//do nothing
		}

		@Override
		public void onAnimationEnd(Animator animator) {
			watch.getFairyHandView().setSource(source.getSourceBow(), INTERVAL, false, new Step4());
		}

		@Override
		public void onAnimationCancel(Animator animator) {
			//do nothing
		}

		@Override
		public void onAnimationRepeat(Animator animator) {
			//do nothing
		}
	};

	class Step4 implements FlipView.FlipViewListener {

		@Override
		public void onFinish() {
			watch.getFairyHandView().setSource(source.getSourceExit(), INTERVAL, false, new Step5());
		}
	}

	class Step5 implements FlipView.FlipViewListener {

		@Override
		public void onFinish() {
			onaction = false;
		}
	}
}
