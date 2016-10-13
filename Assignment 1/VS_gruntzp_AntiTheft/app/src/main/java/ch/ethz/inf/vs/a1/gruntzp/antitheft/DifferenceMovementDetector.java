package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.util.Log;

public class DifferenceMovementDetector extends AbstractMovementDetector {

    private boolean firstMeasurement = true;
    private float[] last = new float[3];

    public DifferenceMovementDetector(AlarmCallback callback, int sensitivity) {
        super(callback, sensitivity);

    }

    @Override
    public boolean doAlarmLogic(float[] values) {
        boolean result = false;
        if (!firstMeasurement) {
            for (int i = 0; i < 3; ++i) {
                float diff = Math.abs(last[i] - values[i]);
                Log.d("difference", Float.toString(diff));
                if (diff > (float)this.sensitivity) {
                    last = values.clone();
                    return true;
                }
            }
        } else { firstMeasurement = false; }
        last = values.clone();
        return false;
    }
}
