package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.JSonSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.RawHttpSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.TextSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.XmlSensor;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class SoapActivity extends AppCompatActivity implements SensorListener{
    public static String host = "vslab.inf.ethz.ch";
    public static String path = "/SunSPOTWebServices/SunSPOTWebservice";
    public static int port = 8080;
    private AbstractSensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap);
    }

    public void getTemperature(View v)
    {
        Button b = (Button) v;
        TextView txtView = (TextView) this.findViewById(R.id.txtValue);
        txtView.setText("loading ...");

        if (b.getText() == getString(R.string.manSoap))
            sensor = new XmlSensor();
        else
            sensor = null;

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
