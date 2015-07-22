package com.nag.android.fairyviewer;

import android.content.Context;

public interface Fairy {
//	void action(Context context, Watch watch, FACTOR factor);
	void onTap(Context context, Watch watch);
	void onShake(Context context, Watch watch);
	void onRolling(Context context, Watch watch);
//	void onStop();
}
