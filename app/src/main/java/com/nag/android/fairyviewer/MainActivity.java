package com.nag.android.fairyviewer;

import com.nag.android.fairyviewer.ShakeManager.OnShakeListener;
import com.nag.android.util.FlipView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements OnShakeListener ,
														TapManager.OnTapListener
														,Watch {
	private ShakeManager shakemanager;
	private TapManager tapmanager;
	private Fairy fairy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		shakemanager = new ShakeManager(this);
		fairy = new FairyBlueGuy(shakemanager);
		findViewById(R.id.relativeLayoutMain).setOnTouchListener(new TapManager(this));
        ((FlipView)findViewById(R.id.animationViewFairy)).setAngleMeter(shakemanager);
	}

    Bitmap b;
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
	public void onShake(Fairy.FACTOR factor)
    {
		fairy.action(this, this, factor);
//        switch(type){
//            case SHAKE:
//                fairy.action(this, this, Fairy.FACTOR.SHAKE);
//                break;
//        }
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
