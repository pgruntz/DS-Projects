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

        TextView tv = (TextView) findViewById(R.id.textView);

        switch (Position){
            case 0:
                tv.setText("Sensor 1 was pressed");
                break;
            case 1:
                tv.setText("Sensor 2 was pressed");
                break;
            case 2:
                tv.setText("Sensor 3 was pressed");
                break;
            case 3:
                tv.setText("Sensor 4 was pressed");
                break;
            case 4:
                tv.setText("Sensor 5 was pressed");
                break;
            case 5:
                tv.setText("Sensor 6 was pressed");
                break;
            case 6:
                tv.setText("Sensor 7 was pressed");
                break;
            case 7:
                tv.setText("Sensor 8 was pressed");
                break;
            default:
                tv.setText("A magic button was pressed");
                break;
        }


    }
}
