package com.nag.android.fairyviewerengine;

import android.content.Context;

public interface Fairy {
	void onTap(Context context, Watch watch);
	void onShake(Context context, Watch watch);
	void onRolling(Context context, Watch watch);
}
