package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.XmlSensor;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class SOAPactivity extends AppCompatActivity implements SensorListener {
    public static String host = "vslab.inf.ethz.ch";
    public static String path = "/SunSPOTWebServices/SunSPOTWebservice";
    public static int port = 8080;

    //On receipt of a value, display it on the screen
    public void onReceiveSensorValue (double value) {
        TextView mTextView = (TextView) this.findViewById(R.id.valueSOAP);
        mTextView.setText(String.valueOf(value));
    }

    //If a message was received from the sonsor, display it as toast
    public void onReceiveMessage(String message) {
        Toast.makeText(getApplicationContext(), "Sensor sent: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soapactivity);
    }

    public void getTemperature(View v) {
        Button mButton = (Button) v;
        TextView mTextView = (TextView) this.findViewById(R.id.valueSOAP);
        mTextView.setText("loading...");
        AbstractSensor sensor;

        //if(mButton.getText().equals(getString(R.string.manualSOAP)))
            sensor = new XmlSensor();

        sensor.registerListener(this);
        sensor.getTemperature();
    }


}
