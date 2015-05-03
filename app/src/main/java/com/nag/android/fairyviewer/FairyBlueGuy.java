package com.nag.android.fairyviewer;

import android.content.Context;

import com.nag.android.util.FlipView;

import static com.nag.android.fairyviewer.Fairy.FACTOR.*;

/**
 * Created by ddiamond on 2015/04/23.
 */
public class FairyBlueGuy implements Fairy,FlipView.FlipViewListener
{
	private static final int INTERVAL = 124;
	private static final int DURATION=3000;
	private Watch watch;

	@Override
	public void action(Context context, Watch watch, FACTOR factor) {
		switch(factor){
			case TAP:
            case ROTATE:
				onAction(context, watch);
				break;
			case SHAKE:
				onShake(context, watch);
				break;
			default:
				throw new UnsupportedOperationException();
		}
	}

	@Override
	public void onStop() {
		// doNothing
	}

	private void onShake(Context context, Watch watch) {
		watch.getFairyHandView().reset();
		watch.getHourHandView().random(DURATION);
		watch.getMinuteHandView().random(DURATION);
	}

	private void onAction(Context context, Watch watch){
		this.watch = watch;
		FlipView fairy = watch.getFairyHandView();
		fairy.setOnFlipViewListener(this);
		watch.getFairyHandView().setSource(R.array.fairy_blueguy_enter, INTERVAL, false);
	}

	@Override
	public void onFinish() {
		watch.getHourHandView().setToNow(DURATION);
		watch.getMinuteHandView().setToNow(DURATION);
	}
}
