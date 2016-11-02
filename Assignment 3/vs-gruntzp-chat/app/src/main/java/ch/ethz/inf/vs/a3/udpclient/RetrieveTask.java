package ch.ethz.inf.vs.a3.udpclient;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;

import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageComparator;
import ch.ethz.inf.vs.a3.queue.PriorityQueue;

/**
 * Created by niederbm on 11/2/16.
 */

public class RetrieveTask extends AsyncTask<String, Void, PriorityQueue<Message>> {

    public interface ResponseHandler {//To access result from ChatActivity when finished
        void HandleResponse(RetrieveTask task);
    }

    private ResponseHandler callback;
    public PriorityQueue<Message> result;
    private static final int MAX_TIMEOUTS = 1;

    public RetrieveTask(ResponseHandler c) {
        this.callback = c;
    }

    @Override
    protected void onPreExecute() {
        //Socket should already exist, just to make sure...
        Helper.makeDatagramSocket();
    }

    @Override
    protected PriorityQueue<Message> doInBackground(String... params) {
        String message = params[0];
        int timeouts = MAX_TIMEOUTS;
        JSONObject jsonReply;
        Message msgReply;


        //Send message to request message retrieval from server
        byte[] sendBuf = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, Helper.address, NetworkConsts.UDP_Port());

        //Create PriorityQueue to store all messages from server. Create receive buffer.
        byte[] recvBuf = new byte[256];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
        PriorityQueue<Message> recvQueue = new PriorityQueue<Message>(new MessageComparator());

        //Send request once
        try {
            Helper.socket.send(sendPacket);
        } catch(IOException e) {
            e.printStackTrace();
        }

        while(timeouts > 0) {

            try {
                Helper.socket.receive(recvPacket);
            } catch(SocketTimeoutException e) {
                Log.d("TIMEOUT", "Socket timed out while retrieving");
                --timeouts; //Socket timed out so we just lower the variable for the number of attempts
                continue;
            } catch(IOException e) {
                e.printStackTrace();
            }

            String reply = new String(recvPacket.getData());

            //Get message from received packet
            try {
                jsonReply = new JSONObject(reply);
            } catch(JSONException e) {
                e.printStackTrace();
                return null;
            }

            //Insert message into priority queue
            try {
                msgReply = new Message(jsonReply);
            } catch(Message.MessageCreationException e) {
                e.printStackTrace();
                return null;
            }
            recvQueue.add(msgReply);

        }

        return recvQueue;
    }

    @Override
    protected void onPostExecute(PriorityQueue<Message> result) {
        this.result = result;
        this.callback.HandleResponse(this);
    }
}
