package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.RawHttpSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.TextSensor;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RESTactivity extends AppCompatActivity implements SensorListener {
    public static String host = "vslab.inf.ethz.ch";
    public static String path = "/sunspots/Spot1/sensors/temperature";
    public static int port = 8081;

    private AbstractSensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restactivity);
    }

    @Override
    public void onReceiveSensorValue(double value) {
        TextView txtView = (TextView) this.findViewById(R.id.txtValue);
        txtView.setText(Double.toString(value));
    }

    @Override
    public void onReceiveMessage(String message) {

    }

    public void getTemperature(View v)
    {
        Button b = (Button) v;
        TextView txtView = (TextView) this.findViewById(R.id.txtValue);
        txtView.setText("loading ...");

        if (b.getText() == getString(R.string.RawHTML))
            sensor = new RawHttpSensor();
        else if (b.getText() == getString(R.string.HttpClass))
            sensor = new TextSensor();
        else if (b.getText() == getString(R.string.JSon))
            sensor = new TextSensor();
            // ändern
        sensor.registerListener(this);
        sensor.getTemperature();
    }
}
