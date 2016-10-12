package ch.ethz.inf.vs.a1.gruntzp.sensors;

import android.hardware.Sensor;

import java.util.Arrays;
import java.util.List;

public class SensorTypesImpl implements SensorTypes {

    private int[] threes = new int[] {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GRAVITY, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_LINEAR_ACCELERATION, Sensor.TYPE_ROTATION_VECTOR};

    public int getNumberValues(int sensorType){

        if(sensorType == Sensor.TYPE_ACCELEROMETER || sensorType == Sensor.TYPE_GRAVITY || sensorType == Sensor.TYPE_MAGNETIC_FIELD || sensorType == Sensor.TYPE_GYROSCOPE || sensorType == Sensor.TYPE_LINEAR_ACCELERATION || sensorType == Sensor.TYPE_ROTATION_VECTOR) {
             return 3;
         }else{
             return 1;
        }
    }

    public String getUnitString(int sensorType){
        switch(sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                return "m/s^2";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "°C";
            case Sensor.TYPE_GRAVITY:
                return "m/s^2";
            case Sensor.TYPE_GYROSCOPE:
                return "rad/s";
            case Sensor.TYPE_LIGHT:
                return "lx";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "µT";
            case Sensor.TYPE_PRESSURE:
                return "hPa";
            case Sensor.TYPE_PROXIMITY:
                return "cm";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "%";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "m/s^2";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "rad/s";
            default:
                return null;
        }
    }
}
