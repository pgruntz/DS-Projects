package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.RawHttpSensor;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RESTactivity extends AppCompatActivity implements SensorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restactivity);

        RawHttpSensor sensor = new RawHttpSensor();
        sensor.registerListener(this);
        sensor.getTemperature();
    }

    @Override
    public void onReceiveSensorValue(double value) {
        TextView txtView = (TextView) this.findViewById(R.id.txtValue);
        txtView.setText(Double.toString(value));
    }

    @Override
    public void onReceiveMessage(String message) {

    }
}
