package com.nag.android.fairyviewer;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class ShakeManager implements SensorEventListener {

	public interface OnShakeListener {
		public void onShake();
	}
	
	private static final int FORCE_THRESHOLD = 300;
	private static final int TIME_THRESHOLD = 100;
	private static final int SHAKE_TIMEOUT = 500;
	private static final int SHAKE_DURATION = 100;
	private static final int SHAKE_COUNT = 2;
	
	private SensorManager mSensorManager;
	private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
	private long mLastTime;
	private OnShakeListener listener;
	//private Context mContext;
	private int mShakeCount = 0;
	private long mLastShake;
	private long mLastForce;

	public ShakeManager(OnShakeListener listener){
		this.listener = listener;
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			long now = System.currentTimeMillis();
			if ((now - mLastForce) > SHAKE_TIMEOUT){
				mShakeCount = 0;
			}
			if((now - mLastTime) > TIME_THRESHOLD){
				long diff = now - mLastTime;
				float speed = Math.abs(event.values[0] +
						event.values[1] +
						event.values[2] -
						mLastX - mLastY - mLastZ ) / diff * 10000;
				if (speed > FORCE_THRESHOLD){
					if((++mShakeCount >= SHAKE_COUNT) && now - mLastShake > SHAKE_DURATION ){
						mLastShake = now;
						mShakeCount = 0;
						if ( listener != null ) {
							listener.onShake();
						}
					}
					mLastForce = now;
				}
				mLastTime = now;
				mLastX = event.values[0];
				mLastY = event.values[1];
				mLastZ = event.values[2];
			}
		}
	}

	public void resume(Context context) {
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if ( mSensorManager == null ) {
			throw new UnsupportedOperationException("Sensor not suported");
		}
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if(sensors.size() > 0) {
			Sensor s = sensors.get(0);
			mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
		}
	}
	
	public void pause() {
		if ( mSensorManager != null ) {
			mSensorManager.unregisterListener(this);
			mSensorManager = null;
		}
	}

}
