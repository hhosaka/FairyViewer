package com.nag.android.fairyviewer;

import android.animation.Animator;
import android.content.Context;

import com.nag.android.util.FlipView;

public class FairyShadowGuy implements Fairy
{
	private static final int INTERVAL = 124;
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

	@Override
	public void onTap(Context context, Watch watch){
		if(!onaction) {
			onaction = true;
			this.watch = watch;
			watch.getFairyHandView().setSource(R.array.fairy_enter, INTERVAL, false, new Step2());
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
			watch.getFairyHandView().setSource(R.array.fairy_windup, INTERVAL, true);
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
			watch.getFairyHandView().setSource(R.array.fairy_bow, INTERVAL, false, new Step4());
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
			watch.getFairyHandView().setSource(R.array.fairy_exit, INTERVAL, false, new Step5());
		}
	}

	class Step5 implements FlipView.FlipViewListener {

		@Override
		public void onFinish() {
			onaction = false;
		}
	}
}
