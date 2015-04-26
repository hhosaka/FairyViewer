package com.nag.android.fairyviewer;

/**
 * Created by ddiamond on 2015/04/24.
 */
public interface Watch {
	WatchHandView getHourHandView();
	WatchHandView getMinuteHandView();
	com.nag.android.util.FlipView getFairyHandView();
}
