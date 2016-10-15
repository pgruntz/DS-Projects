package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.util.Log;

import ch.ethz.inf.vs.a2.http.HttpRawRequest;

/**
 * Created by PhiSc on 14.10.2016.
 * It should generate a raw HTTP GET request to obtain the temperature information of Spot1
 *
 * http://www.elektronik-kompendium.de/sites/net/0902231.htm
 */

public class HttpRawRequestImpl implements HttpRawRequest {

    //public static String uri = "http://vslab.inf.ethz.ch:8081/sunspots/Spot1/sensors/temperature";
    public static String nL = "\r\n";

    @Override
    public String generateRequest(String host, int port, String path) {
        String request = String.format( "GET " + path + " HTTP/1.1" + nL +
                                        "Host: "+ host + ":" + port + nL +
                                        "Connection: close" + nL +
                                        "Accept: text/html" + nL +
                                        nL,
                host, path, port);
        Log.d("sending: ", request);
        return request;
    }
}
