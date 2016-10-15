package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.RESTactivity;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by PhiSc on 15.10.2016.
 */

public class JSonSensor extends AbstractSensor {
    @Override
    public String executeRequest() throws Exception {
        URL url = new URL("http://" + RESTactivity.host + ":" + RESTactivity.port + RESTactivity.path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
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
        try {
            JSONObject jo = new JSONObject(response);
            return 0;
        } catch (JSONException e) {
            return 0;
        }
    }
}
