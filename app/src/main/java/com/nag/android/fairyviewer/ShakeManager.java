package com.nag.android.fairyviewer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

import com.nag.android.util.AngleMeter;

public class ShakeManager implements SensorEventListener, AngleMeter {

	public interface OnShakeListener {
		void onShake();
	}

	public interface OnRotationListener {
		void OnChangeLevel();
	}

	private static final int FORCE_THRESHOLD = 300;
	private static final int TIME_THRESHOLD = 100;
	private static final int SHAKE_TIMEOUT = 500;
	private static final int SHAKE_DURATION = 100;
	private static final int SHAKE_COUNT = 2;

	private SensorManager sensormanager;
//	private float mLastX = -1.0f, lasty = -1.0f, lastz = -1.0f;
    private float[] prev = new float[3];
	private long mLastTime;
	private OnShakeListener listener;
	//private Context mContext;
	private int mShakeCount = 0;
	private long mLastShake;
	private long mLastForce;
	private OnRotationListener listenerLevel= null;

	public void setOnLevelListener(OnRotationListener listener){
		this.listenerLevel = listener;
	}

	public ShakeManager(OnShakeListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
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
        checkLevel(event);
        checkShake(event);
    }

    private void checkShake(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long now = System.currentTimeMillis();
            if ((now - mLastForce) > SHAKE_TIMEOUT) {
                mShakeCount = 0;
            }
            if ((now - mLastTime) > TIME_THRESHOLD) {
                long diff = now - mLastTime;
                float speed = Math.abs(sum(event.values)-sum(prev)) / diff * 10000;
                if (speed > FORCE_THRESHOLD) {
                    if ((++mShakeCount >= SHAKE_COUNT) && now - mLastShake > SHAKE_DURATION) {
                        mLastShake = now;
                        mShakeCount = 0;
                        if (listener != null) {
                            listener.onShake();
                        }
                    }
                    mLastForce = now;
                }
                mLastTime = now;
                prev = event.values.clone();
            }
        }
    }

    public void resume(Context context) {
		sensormanager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (sensormanager == null) {
			throw new UnsupportedOperationException("Sensor not suported");
		}
        sensormanager.registerListener(this, sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
        sensormanager.registerListener(this,sensormanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_UI);
//		List<Sensor> sensors = sensormanager.getSensorList(Sensor.TYPE_ACCELEROMETER);
//		if (sensors.size() > 0) {
//			Sensor s = sensors.get(0);
//			sensormanager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
//		}
	}

	public void pause() {
		if (sensormanager != null) {
			sensormanager.unregisterListener(this);
			sensormanager = null;
		}
	}

    float[]geomagnetic = null;
    float[]gravity = null;
    float[]attitude = new float[3];
    float[]rotationMatrix = new float[9];
    private final static double RAD2DEG = 180/Math.PI;

	private void checkLevel(SensorEvent event) {

        switch(event.sensor.getType()){
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
        }

        if(geomagnetic != null && gravity != null){
            SensorManager.getRotationMatrix(rotationMatrix, null,gravity, geomagnetic);
            SensorManager.getOrientation(rotationMatrix,attitude);
            if(listenerLevel!=null){
//                listenerLevel.OnChangeLevel(getRoll(),getPitch(),getAzimuth());
                listenerLevel.OnChangeLevel();
            }
        }
	}

    @Override
    public int getAngle(){
        return getY()>0?getX():180-getX();
    }

    private  int getX(){
        return (int)(gravity[0]* 9);
    }

    private  int getY(){
        return (int)(gravity[1]* 9);
    }

    private  int getZ(){
        return (int)(gravity[2]* 9);
    }
}
