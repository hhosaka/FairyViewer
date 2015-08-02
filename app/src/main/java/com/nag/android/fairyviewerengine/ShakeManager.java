package com.nag.android.fairyviewerengine;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

import com.nag.android.util.AngleMeter;

public class ShakeManager implements SensorEventListener, AngleMeter {
	public interface OnShakeListener {
		void onShake();
        void onRolling();
	}

	private static final int FORCE_THRESHOLD = 300;
	private static final int TIME_THRESHOLD = 100;
	private static final int ANGLE_THRESHOLD = 60;
	private static final int SHAKE_TIMEOUT = 500;
	private static final int SHAKE_DURATION = 100;
	private static final int SHAKE_COUNT = 3;

    private float[]gravity = new float[3];
    private long timePrev = 0;
    private long timeLastShake = 0;
    private long timeLastForce = 0;
    private int countShake = 0;
	private int prevAngle = 0;


    private SensorManager sensormanager;
	private OnShakeListener listener;

	public ShakeManager(OnShakeListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
        //do nothing
	}

    public void resume(Context context) {
		sensormanager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (sensormanager == null) {
			throw new UnsupportedOperationException("Sensor not suported");
		}
        sensormanager.registerListener(this, sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
        sensormanager.registerListener(this,sensormanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_UI);
	}

	public void pause() {
		if (sensormanager != null) {
			sensormanager.unregisterListener(this);
			sensormanager = null;
		}
	}

    private float sum(float[] values){
        float ret = 0;
        for(float value :  values ){
            ret += value;
        }
        return ret;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long now = System.currentTimeMillis();
        if ((now - timePrev) > TIME_THRESHOLD) {
            checkShake(event, now);
            timePrev = now;
            gravity = event.values.clone();
        }
    }

    private void checkShake(SensorEvent event, long now) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if ((now - timeLastForce) > SHAKE_TIMEOUT) {
                countShake = 0;
            }
            long diff = now - timePrev;
            float speed = Math.abs(sum(event.values)-sum(gravity)) / diff * 10000;
            if (speed > FORCE_THRESHOLD) {
                if ((++countShake >= SHAKE_COUNT) && now - timeLastShake > SHAKE_DURATION) {
                    timeLastShake = now;
                    countShake = 0;
                    if (listener != null) {
                        listener.onShake();
                    }
                }
                timeLastForce = now;
            }
			if(normalizeAngle(prevAngle - getAngle())>ANGLE_THRESHOLD){
				setAngle();
				listener.onRolling();
			}
        }
    }

	private void setAngle(){
		prevAngle = getAngle();
	}

    @Override
    public int getAngle(){
        return getY()>0?getX():180-getX();
    }
	private int normalizeAngle(int angle){return (angle+360)%360;}
    private  int getX(){
        return (int)(gravity[0]* 9);
    }
    private  int getY(){
        return (int)(gravity[1]* 9);
    }
}
