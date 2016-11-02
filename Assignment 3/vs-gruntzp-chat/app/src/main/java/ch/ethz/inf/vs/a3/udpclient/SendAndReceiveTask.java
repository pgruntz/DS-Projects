package ch.ethz.inf.vs.a3.udpclient;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.UUID;

import ch.ethz.inf.vs.a3.message.MessageTypes;

/**
 * Created by PhiSc on 26.10.2016.
 */
public class SendAndReceiveTask extends AsyncTask<String, Void, String> {

    public interface ResponseHandler {
        void HandleResponse(SendAndReceiveTask task, Object... passthrough);
    }

    public Object[] passthrough;
    private ResponseHandler callback;

    public SendAndReceiveTask(ResponseHandler callback, Object... passthrough) {
        super();
        this.passthrough = passthrough;
        this.callback = callback;
    }

    public JSONObject result = null;
    public String usedUUID = null;

    protected void onPreExecute() {
        //building the socket
        Helper.makeDatagramSocket();
    }

    public static final String RANOM_UUID = "random";

    @Override
    protected String doInBackground(String... params) {
        String message = params[0];

        if (params.length > 1) {// has uuid in parameter but not in message -> generate Random or insert
            // pass uuid in arg1 or create a random one
            usedUUID = (params[1].equals(RANOM_UUID)) ?
                    UUID.randomUUID().toString() :
                    params[1];
            // If message needs needs insertion of uuid, insert now. Otherwise it does nothing
            message = String.format(message, usedUUID);
        }

        byte[] sendBuf = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, Helper.address, NetworkConsts.UDP_Port());

        //preparing response message
        byte[] recBuf = new byte[256];
        DatagramPacket responsePacket = new DatagramPacket(recBuf, recBuf.length);

        try {
            Helper.socket.send(sendPacket);
            Helper.socket.receive(responsePacket);
            String received = new String(responsePacket.getData());
            return received;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {

            Log.d("received", result);
            try {
                this.result = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.callback.HandleResponse(this);
    }
}
