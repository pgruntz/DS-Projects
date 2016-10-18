package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.JSonSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.RawHttpSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.SoapSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.TextSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.XmlSensor;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class SoapActivity extends AppCompatActivity implements SensorListener{
    public static String soap_request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"+
                                        "    <S:Header/>\n"+
                                        "    <S:Body>\n"+
                                        "        <ns2:getSpot xmlns:ns2=\"http://webservices.vslecture.vs.inf.ethz.ch/\">\n"+
                                        "            <id>Spot3</id>\n"+
                                        "        </ns2:getSpot>\n"+
                                        "    </S:Body>\n"+
                                        "</S:Envelope>";

    public static String host = "vslab.inf.ethz.ch";
    public static String path = "/SunSPOTWebServices/SunSPOTWebservice";
    public static int port = 8080;
    public static String protocol;
    static {
        protocol = "http://" + host + ":" + port + path;
    }
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
            sensor = new SoapSensor();

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
