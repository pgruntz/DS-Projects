package ch.ethz.inf.vs.a3.udpclient;

import android.os.AsyncTask;
import android.provider.ContactsContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by PhiSc on 25.10.2016.
 */

public class Helper {
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

    public static String JSONmessage(String uuid, String username, String type) throws JSONException {
        return JSONmessage(uuid, username, new JSONObject().toString(), type, new JSONObject());
    }

    public static class NetworkRunnable implements Runnable {
        private DatagramPacket sendP;
        public DatagramPacket recP;
        private DatagramSocket s;

        public NetworkRunnable(DatagramSocket s, DatagramPacket sendP, DatagramPacket recP)
        {
            this.s = s;
            this.sendP = sendP;
            this.recP = recP;
        }
        @Override
        public void run() {
            try {
                s.send(sendP);
                s.receive(recP);
            } catch (IOException e) {
                recP = null;
                e.printStackTrace();
            }

        }
    }
}
