package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by PhiSc on 07.10.2016.
 */

public class MovementDetector extends AbstractMovementDetector implements SensorEventListener {
    public MovementDetector(AlarmCallback callback, int sensitivity) {
        super(callback, sensitivity);
    }

    @Override
    public boolean doAlarmLogic(float[] values) {
        float sum = values[0] + values[1] + values[2];
        sum = Math.abs(sum);
        System.out.println(sum);
        return sum > this.sensitivity;
    }
}
