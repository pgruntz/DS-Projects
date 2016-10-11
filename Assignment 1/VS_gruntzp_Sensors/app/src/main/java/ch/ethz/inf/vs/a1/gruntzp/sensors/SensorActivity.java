package ch.ethz.inf.vs.a1.gruntzp.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import java.math.BigDecimal;
import java.util.Arrays;

public class SensorActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorM;
    private Sensor currentSensor;
    private TextView tv;
    private String sensorName;
    private SensorTypesImpl sensorType = new SensorTypesImpl();

    private GraphView graph;
    private GraphContainer graphContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("Position");
        sensorName = extras.getString("sensorName");


        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Sensor not found ("+sensorName+")");


        sensorM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        int[] const_sensors = new int[]{Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_GRAVITY,
                Sensor.TYPE_GYROSCOPE, Sensor.TYPE_LIGHT, Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_PRESSURE,
                Sensor.TYPE_PROXIMITY, Sensor.TYPE_RELATIVE_HUMIDITY};
        assert (position < const_sensors.length);
        currentSensor = sensorM.getDefaultSensor(const_sensors[position]);

        timestamp = System.currentTimeMillis();

        graph = (GraphView) findViewById(R.id.graph);
        graphContainer = new GraphContainerImpl(graph);



    }

    public GraphContainer getGraphContainer() {
        return graphContainer;
    }

    static long timestamp = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] copy = event.values.clone();
        long xCoord = System.currentTimeMillis()-timestamp;

        int size = sensorType.getNumberValues(currentSensor.getType());
        String unit = sensorType.getUnitString(currentSensor.getType());
        if (size == 1){
            tv.setText(sensorName + "\n\nValue: " + Float.toString(copy[0]));
        } else{
            tv.setText(sensorName + "\n\nValues:\n" +
                    new BigDecimal(Float.toString(copy[0])).setScale(3,BigDecimal.ROUND_HALF_UP).toString() + "/" +
                    new BigDecimal(Float.toString(copy[1])).setScale(3,BigDecimal.ROUND_HALF_UP).toString() + "/" +
                    new BigDecimal(Float.toString(copy[2])).setScale(3,BigDecimal.ROUND_HALF_UP).toString());
        }

        graph.getGridLabelRenderer().setVerticalAxisTitle(unit);

        if(size==1){
            graphContainer.addValues(xCoord,new float[]{copy[0]});
        }else{
            graphContainer.addValues(xCoord,copy);
        }
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


