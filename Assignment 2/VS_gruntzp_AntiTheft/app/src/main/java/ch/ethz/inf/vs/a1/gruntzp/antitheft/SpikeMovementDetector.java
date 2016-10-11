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
        return sum > this.sensitivity;
    }
}
