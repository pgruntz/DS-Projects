package ch.ethz.inf.vs.a1.gruntzp.sensors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class SensorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Bundle extras = getIntent().getExtras();
        int Position = extras.getInt("Position");
        String sensorName = extras.getString("sensorName");

        TextView tv = (TextView) findViewById(R.id.textView);

        tv.setText(sensorName + " (Button Nr.: " + Integer.toString(Position) + ")");




    }
}
