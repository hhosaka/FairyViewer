package com.nag.android.fairyviewerengine;

import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;

import com.nag.android.fairyviewerengine.WatchHandView;

public class HourHandView extends WatchHandView {

	public HourHandView(Context context) {
		super(context);
	}

	public HourHandView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HourHandView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected float getAngle() {
		Time time = new Time();
		time.setToNow();
		return 360.0f/12 * ((time.hour+1)%12-1)+360.0f/12/60*time.minute;
	}
}
