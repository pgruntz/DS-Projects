package ch.ethz.inf.vs.a1.gruntzp.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

import static android.R.attr.value;

public class SensorActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorM;
    private Sensor currentSensor;
    private TextView tv;
    private String sensorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("Position");
        sensorName = extras.getString("sensorName");


        tv = (TextView) findViewById(R.id.textView);
        tv.setText(sensorName + " (Button Nr.: " + Integer.toString(position) + ")");

        sensorM = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        switch(position){
            case 0:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                break;
            case 1:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
                break;
            case 2:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_GRAVITY);
                break;
            case 3:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                break;
            case 4:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_LIGHT);
                break;
            case 5:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                break;
            case 6:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_PRESSURE);
                break;
            case 7:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                break;
            case 8:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
                break;
            case 9:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
                break;

            default:
                currentSensor = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                break;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float val = event.values[0];
        tv.setText(sensorName + ": " + Float.toString(val));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        tv.setText("Accuracy changed");

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorM.registerListener(this, currentSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorM.unregisterListener(this);
    }
}
