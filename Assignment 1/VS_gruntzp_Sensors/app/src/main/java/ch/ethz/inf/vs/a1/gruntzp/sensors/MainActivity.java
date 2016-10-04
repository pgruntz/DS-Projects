package ch.ethz.inf.vs.a1.gruntzp.sensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView ls = (ListView) findViewById(R.id.sensor_list);
        ls.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String sensorName=getResources().getStringArray(R.array.sensors_array)[position];

        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra("Position",position);
        intent.putExtra("sensorName",sensorName);




        startActivity(intent);
    }
}
