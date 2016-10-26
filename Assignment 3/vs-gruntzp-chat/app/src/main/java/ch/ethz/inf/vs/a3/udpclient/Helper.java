package ch.ethz.inf.vs.a3.udpclient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by PhiSc on 25.10.2016.
 */

public class Helper {

    // Share with all requests
    public static DatagramSocket socket;
    public static InetAddress address;

    // Build JSON Message for Chat
    public static String JSONmessage(String uuid, String username, String timestamp, String type, JSONObject Body) throws JSONException {
        JSONObject o = new JSONObject();

        JSONObject header = new JSONObject();
        header.put("username", username);
        header.put("uuid", uuid);
        header.put("timestamp", timestamp);
        header.put("type", type);

        o.put("header", header);
        o.put("body", Body);
        return o.toString(2);
    }


    // Can be called often. Only has effects once.
    public static synchronized boolean makeDatagramSocket() {
        if (socket == null) {
            try {
                socket = new DatagramSocket(NetworkConsts.UDP_PORT);
                socket.setSoTimeout(NetworkConsts.SOCKET_TIMEOUT);
                address = InetAddress.getByName(NetworkConsts.SERVER_ADDRESS);
            } catch (SocketException e) {
                e.printStackTrace();
                return false;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    // Simple request
    public static String JSONmessage(String uuid, String username, String type) throws JSONException {
        return JSONmessage(uuid, username, new JSONObject().toString(), type, new JSONObject());
    }

    public static String JSONmessage (String username, String type) throws JSONException {
        return JSONmessage("%s", username, type);
    }

    public static final int REGISTER_REQUEST = 0;
    public static final int DEREGISTER_REQUEST = 1;
}
