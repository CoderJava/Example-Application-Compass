package com.ysn.exampleapplicationcompass.library;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.ysn.exampleapplicationcompass.views.base.Presenter;
import com.ysn.exampleapplicationcompass.views.base.View;
import com.ysn.exampleapplicationcompass.views.main.MainActivityPresenter;

/**
 * Created by root on 17/04/17.
 */

public class Compass implements SensorEventListener {
    private final String TAG = "CompassTAG";
    private SensorManager sensorManager;
    private Sensor gsensor;
    private Sensor msensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currentAzimuth = 0;

    // compass arrow to rotate
    /*public ImageView imageViewArrowView = null;*/
    private String className;
    private Presenter presenter;

    public Compass(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start(Presenter presenter, String className) {
        this.className = className;
        this.presenter = presenter;
        sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * sensorEvent.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * sensorEvent.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * sensorEvent.values[2];

                /*mGravity == sensorEvent.values;*/
                /*Log.d(TAG, Float.toString(mGravity[0]));*/
            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                /*mGeomagnetic = sensorEvent.values;*/

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * sensorEvent.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * sensorEvent.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * sensorEvent.values[2];
                /*Log.d(TAG, Float.toString(sensorEvent.values[0]));*/
            }

            float r[] = new float[9];
            float i[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(r, i, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(r, orientation);
                /*Log.d(TAG, "azimuth (rad): " + azimuth);*/
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation
                azimuth = (azimuth + 360) % 360;
                /*Log.d(TAG, "azimuth (deg): " + azimuth);*/
                /*onAdjustArrow();*/
                // mainActivityPresenter.onAdjustArrow(-currentAzimuth, -azimuth);
                if (className.equalsIgnoreCase("MainActivity")) {
                    MainActivityPresenter mainActivityPresenter = (MainActivityPresenter) presenter;
                    mainActivityPresenter.onAdjustArrow(-currentAzimuth, -azimuth);
                }
                currentAzimuth = azimuth;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // nothing to do in here
    }
}
