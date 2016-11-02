package ch.ethz.inf.vs.a3.udpclient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ch.ethz.inf.vs.a3.message.MessageTypes;

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
                socket = new DatagramSocket(NetworkConsts.UDP_Port());
                socket.setSoTimeout(NetworkConsts.SOCKET_TIMEOUT);
                address = InetAddress.getByName(NetworkConsts.Server_Adress());
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

    //deregister with only one attempt
    public static void deregister(String username, String uuid, SendAndReceiveTask.ResponseHandler handler)
    {
        SendAndReceiveTask t = new SendAndReceiveTask(handler, Helper.DEREGISTER_REQUEST);
        try {
            String message = Helper.JSONmessage(uuid, username, MessageTypes.DEREGISTER);
            t.execute(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final int REGISTER_REQUEST = 0;
    public static final int DEREGISTER_REQUEST = 1;
}
