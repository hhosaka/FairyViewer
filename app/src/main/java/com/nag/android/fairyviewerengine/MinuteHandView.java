package com.nag.android.fairyviewerengine;

import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;

import com.nag.android.fairyviewerengine.WatchHandView;

public class MinuteHandView extends WatchHandView {

	public MinuteHandView(Context context) {
		super(context);
	}

	public MinuteHandView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MinuteHandView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected float getAngle() {
		Time time = new Time();
		time.setToNow();
		return 360.0f/60*time.minute;
	}
}
