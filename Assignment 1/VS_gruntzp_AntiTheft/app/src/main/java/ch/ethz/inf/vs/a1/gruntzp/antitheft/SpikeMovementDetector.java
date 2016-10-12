package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import ch.ethz.inf.vs.a1.gruntzp.antitheft.AlarmCallback;

public class SpikeMovementDetector extends AbstractMovementDetector {

    public SpikeMovementDetector(AlarmCallback callback, int sensitivity) {
        super(callback, sensitivity);
    }

    @Override
    public boolean doAlarmLogic(float[] values) {
        float sum = values[0] + values[1] + values[2];
        sum = Math.abs(sum);
        System.out.println(sum);
        // -10 because we don't have linear accelerometer
        return sum - 9.81 > this.sensitivity;
    }
}
