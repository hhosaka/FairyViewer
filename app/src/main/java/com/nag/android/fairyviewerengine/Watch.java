package com.nag.android.fairyviewerengine;

import com.nag.android.util.FlipView;

/**
 * Created by ddiamond on 2015/04/24.
 */
public interface Watch {
	public interface OnFinishListener{
		void onFinish();
	}
	WatchHandView getHourHandView();
	WatchHandView getMinuteHandView();
	FlipView getFairyHandView();
}
