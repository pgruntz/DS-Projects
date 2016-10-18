package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.*;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;



/**
 * Created by PhiSc on 16.10.2016.
 */

public class XmlSensor extends AbstractSensor {
    private static String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"+
                                "    <S:Header/>\n"+
                                "    <S:Body>\n"+
                                "        <ns2:getSpot xmlns:ns2=\"http://webservices.vslecture.vs.inf.ethz.ch/\">\n"+
                                "            <id>Spot3</id>\n"+
                                "        </ns2:getSpot>\n"+
                                "    </S:Body>\n"+
                                "</S:Envelope>";

    @Override
    public String executeRequest() throws Exception {
        URL url = new URL("http://" + SoapActivity.host + ":" + SoapActivity.port + SoapActivity.path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; encoding='utf-8'");
        //connection.setRequestProperty("Connection", "close");

        try {
            OutputStream output = new BufferedOutputStream(connection.getOutputStream());
            output.write(data.getBytes());
            output.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String rs = null;
            while ((rs = in.readLine()) != null)
            {
                sb.append(rs + "\n");
            }
            String response = sb.toString();
            return response;
        } finally {
            connection.disconnect();
        }

    }

    @Override
    public double parseResponse(String response) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String currentTag = parser.getName();
                if (currentTag != null && currentTag.equals("temperature")) {
                    String value = parser.nextText();
                    return Double.parseDouble(value);
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }


}

