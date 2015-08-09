package com.nag.android.fairyviewerengine;

import android.content.Context;

public interface Fairy {
	void init(Context context, Watch watch);
	void onTap(Context context, Watch watch);
	void onShake(Context context, Watch watch);
	void onRolling(Context context, Watch watch);
}
