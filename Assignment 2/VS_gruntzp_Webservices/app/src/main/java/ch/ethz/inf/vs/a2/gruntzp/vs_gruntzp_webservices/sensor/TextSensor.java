package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.RESTactivity;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by PhiSc on 15.10.2016.
 */

public class TextSensor extends AbstractSensor {

    @Override
    public String executeRequest() throws Exception {
        URL url = new URL("http://" + RESTactivity.host + ":" + RESTactivity.port + RESTactivity.path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "text/plain");
        connection.setRequestProperty("Connection", "close");

        try {
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
//        Pattern p = Pattern.compile("class=\"getterValue\">([^<]*)<\\/span>");
//        Matcher matcher = p.matcher(response);
//        if (matcher.find()) {
//            String value = matcher.group(1);
//            return Double.valueOf(value);
//        } else
//            return 0;
        return Double.valueOf(response.trim());
    }
}
