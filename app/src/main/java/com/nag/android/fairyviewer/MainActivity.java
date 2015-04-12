package com.nag.android.fairyviewer;

import com.nag.android.fairyviewer.ShakeManager.OnShakeListener;
import com.nag.android.util.FlipView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnShakeListener ,
														FlipView.FlipViewListener,
														TapManager.OnTapListener{
	private FlipView fairy;
	private WatchHandView hourhand;
	private WatchHandView minutehand;
	private ShakeManager shakemanager;
	private TapManager tapmanager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fairy = (FlipView)findViewById(R.id.animationViewFairy);
		fairy.setOnFlipViewListener(this);
		hourhand = (WatchHandView)findViewById(R.id.imageViewHourHand);
		minutehand = (WatchHandView)findViewById(R.id.imageViewMinuteHand);
		shakemanager = new ShakeManager(this);
		findViewById(R.id.relativeLayoutMain).setOnTouchListener(new TapManager(this));
	}

	@Override
	protected void onResume() {
		super.onResume();
		shakemanager.resume(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		shakemanager.pause();
	}

	private static final int DURATION=3000;
	private static final int INTERVAL = 124;

	@Override
	public void onShake() {
		fairy.reset();
		hourhand.random(DURATION);
		minutehand.random(DURATION);
	}

	@Override
	public void onFinish() {
		hourhand.adjust(DURATION);
		minutehand.adjust(DURATION);
	}

	@Override
	public void onTap() {
		fairy.setSource(R.array.fairy_blueguy_enter, INTERVAL, false);
	}
}
