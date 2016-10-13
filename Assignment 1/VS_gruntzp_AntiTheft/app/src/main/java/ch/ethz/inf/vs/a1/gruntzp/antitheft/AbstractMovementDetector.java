package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import ch.ethz.inf.vs.a1.gruntzp.antitheft.AlarmCallback;

public abstract class AbstractMovementDetector implements SensorEventListener {

    public static final boolean useNonLinearSensor = false;


    protected AlarmCallback callback;
    protected int sensitivity;

    public AbstractMovementDetector(AlarmCallback callback, int sensitivity){
        this.callback = callback;
        this.sensitivity = sensitivity;
    }

    // Sensor monitoring
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("info", "Our test device did not have an linear accelerometer!");
        if (useNonLinearSensor || event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Copy values because the event is not owned by the application
            float[] values = event.values.clone();
            if(doAlarmLogic(values)){
                callback.onDelayStarted();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do not do anything
    }

    public abstract boolean doAlarmLogic(float[] values);

}
