package com.nag.android.fairyviewer;

import com.nag.android.fairyviewer.ShakeManager.OnShakeListener;
import com.nag.android.util.FlipView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity implements OnShakeListener ,
														TapManager.OnTapListener
														,Watch{
	private ShakeManager shakemanager;
	private TapManager tapmanager;
	private Fairy fairy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fairy = new FairyBlueGuy();
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

	@Override
	public void onShake() {
		fairy.action(this, this, Fairy.FACTOR.SHAKE);
	}

	@Override
	public void onTap() {
		fairy.action(this, this, Fairy.FACTOR.TAP);
	}

	@Override
	public WatchHandView getHourHandView() {
		return (WatchHandView)findViewById(R.id.imageViewHourHand);
	}

	@Override
	public WatchHandView getMinuteHandView() {
		return (WatchHandView)findViewById(R.id.imageViewMinuteHand);
	}

	@Override
	public FlipView getFairyHandView() {
		return (FlipView)findViewById(R.id.animationViewFairy);
	}
}
