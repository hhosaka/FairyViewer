package com.nag.android.fairyviewer;

import android.content.Context;

public interface Fairy {
	enum FACTOR{TAP,SHAKE,ROTATE};
	void action(Context context, Watch watch, FACTOR factor);
	void onStop();
}
