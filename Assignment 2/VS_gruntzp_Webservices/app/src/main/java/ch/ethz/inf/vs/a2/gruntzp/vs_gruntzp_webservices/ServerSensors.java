package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Patrick on 10/21/2016.
 */

public class ServerSensors extends AppCompatActivity implements SensorEventListener {
    private String sensor1Val;
    private String sensor2Val;
    private SensorManager sensorM;
    private Sensor sensor1;
    private Sensor sensor2;


    public ServerSensors(SensorManager sensMananger){
        this.sensorM = sensMananger;
        sensor1 = sensorM.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor2 = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sensor2 = sensorM.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        sensor1Val="no value";
        sensor2Val="no value";

        sensorM.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
        sensorM.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener(){
        sensorM.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float copy = event.values.clone()[0];

        if(event.sensor.equals(sensor1))
            sensor1Val = Float.toString(copy);
        if(event.sensor.equals(sensor2))
            sensor2Val = Float.toString(copy);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        sensorM.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
        sensorM.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorM.unregisterListener(this);
    }

    public String getSensorValue(int sensorNumber){
        if(sensorNumber==1) return sensor1Val;
        if(sensorNumber==2) return sensor2Val;
        return "wrong sensorNumber";
    }
}
