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
        void HandleResponse(SendAndReceiveTask task);
    }

    public Object[] passthrough;
    private ResponseHandler callback;

    public SendAndReceiveTask(ResponseHandler callback, Object... passthrough) {
        super();
        this.passthrough = passthrough;
        this.callback = callback;
    }

    public JSONObject result = null;

    protected void onPreExecute() {
        //building the socket
        Helper.makeDatagramSocket();
    }

    @Override
    protected String doInBackground(String... params) {
        // pass uuid or create a random one
        //UUID test = UUID.randomUUID();
        String uuid = (params[1].equals("")) ?
                UUID.randomUUID().toString() :
                params[1];

        String message = null;
        try {
            message = Helper.JSONmessage(uuid, params[0], MessageTypes.REGISTER);
        } catch (JSONException e) {
            return null;
        }
        byte[] sendBuf = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, Helper.address, NetworkConsts.UDP_PORT);

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
        if (result == null) return;

        Log.d("received", result);
        try {
            this.result = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.callback.HandleResponse(this);
    }
}
