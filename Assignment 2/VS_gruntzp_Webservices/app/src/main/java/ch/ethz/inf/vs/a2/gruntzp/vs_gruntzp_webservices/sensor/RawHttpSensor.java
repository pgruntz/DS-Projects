package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.HttpRawRequestImpl;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.RESTactivity;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by PhiSc on 14.10.2016.
 */

public class RawHttpSensor extends AbstractSensor {

    @Override
    public String executeRequest() throws Exception {
        String request = (new HttpRawRequestImpl()).generateRequest(RESTactivity.host, RESTactivity.port, RESTactivity.path);
        Socket s = new Socket(InetAddress.getByName(RESTactivity.host), RESTactivity.port);

        PrintWriter out = new PrintWriter(s.getOutputStream());
        out.print(request);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String rs = null;
        while ((rs = in.readLine()) != null)
        {
            sb.append(rs + "\n");
        }
        String response = sb.toString();
        return response;

    }

    @Override
    public double parseResponse(String response) {
        Pattern p = Pattern.compile("class=\"getterValue\">([^<]*)<\\/span>");
        Matcher matcher = p.matcher(response);
        if (matcher.find()) {
            String value = matcher.group(1);
            return Double.valueOf(value);
        } else
            return 0;
    }
}
