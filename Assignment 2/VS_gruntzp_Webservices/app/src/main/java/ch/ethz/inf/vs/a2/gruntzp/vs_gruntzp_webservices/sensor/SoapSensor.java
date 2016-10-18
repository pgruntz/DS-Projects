package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.SoapActivity;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by PhiSc on 18.10.2016.
 */

public class SoapSensor extends AbstractSensor {
    @Override
    public String executeRequest() throws Exception {
        SoapObject so = new SoapObject("http://webservices.vslecture.vs.inf.ethz.ch/", "getSpot");
        so.addProperty("id", "spot3");

        SoapSerializationEnvelope ssEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        ssEnvelope.setOutputSoapObject(so);

        try {
            HttpTransportSE transport = new HttpTransportSE(SoapActivity.protocol);
            transport.debug = true;
            transport.call("", ssEnvelope);
            SoapObject responseObject = (SoapObject) ssEnvelope.getResponse();
            String response = responseObject.getPropertyAsString("temperature");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public double parseResponse(String response) {
        return Double.parseDouble(response);
    }
}
