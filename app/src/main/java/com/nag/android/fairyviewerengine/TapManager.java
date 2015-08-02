package com.nag.android.fairyviewerengine;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TapManager implements OnTouchListener{

	private static final int TIME_THRESH = 1000;
	private static final int COUNT_THRESH = 2;
	private long prev = 0;
	private int count = 0;
	public interface OnTapListener{
		void onTap();
	}
	private OnTapListener listener = null;

	public TapManager(OnTapListener listener){
		this.listener = listener;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			long now = System.currentTimeMillis();
			if(now > prev + TIME_THRESH){
				count = 1;
			}else{
				if(++count>=COUNT_THRESH){
					count = 0;
					if(listener!=null){
						listener.onTap();
					}
				}
			}
			prev = now;
		}
		view.performClick();
		return true;
	}

}
