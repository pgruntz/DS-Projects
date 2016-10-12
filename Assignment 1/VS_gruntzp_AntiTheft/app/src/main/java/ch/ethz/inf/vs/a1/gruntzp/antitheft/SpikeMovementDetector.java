package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import ch.ethz.inf.vs.a1.gruntzp.antitheft.AlarmCallback;

public class SpikeMovementDetector extends AbstractMovementDetector {

    public SpikeMovementDetector(AlarmCallback callback, int sensitivity) {
        super(callback, sensitivity);
    }


    @Override
    public boolean doAlarmLogic(float[] values) {
        float sum = Math.abs(values[0]) + Math.abs(values[1]) + Math.abs(values[2]);
        System.out.println(sum);
        // -gravitiy because we neither have a linear accelerometer on the emulator nor on the phone
        if (AbstractMovementDetector.useNonLinearSensor) sum -= 9.81;
        return sum >= this.sensitivity;
    }
}
