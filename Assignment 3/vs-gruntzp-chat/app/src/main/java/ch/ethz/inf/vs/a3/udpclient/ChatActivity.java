package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.UUID;

import ch.ethz.inf.vs.a2.gruntzp.vsgruntzpchat.R;
import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageTypes;
import ch.ethz.inf.vs.a3.queue.PriorityQueue;

public class ChatActivity extends AppCompatActivity implements SendAndReceiveTask.ResponseHandler, RetrieveTask.ResponseHandler {

    private String uuid;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");
        username = intent.getStringExtra("username");

        TextView textView = (TextView) findViewById(R.id.ChatName);
        textView.setText("Logged in as " + username);
    }


    public void onRetrieveClicked(View view) {
        TextView textView = (TextView) findViewById(R.id.ChatWindow);
        textView.setText("Contacting server...");

        RetrieveTask t = new RetrieveTask(this);

        try {
            String retrieveMsg = Helper.JSONmessage(uuid, username, MessageTypes.RETRIEVE_CHAT_LOG);
            t.execute(retrieveMsg);
        } catch(JSONException e) {
            e.printStackTrace();
            textView.setText("Retrieval failed");
            return;
        }

    }

    @Override
    public void HandleResponse(SendAndReceiveTask task, Object... passthrough) {
        if ((int) task.passthrough[0] == Helper.DEREGISTER_REQUEST)
            deregisterCallback(task);
        else
            throw new UnknownError();
    }

    @Override
    public void HandleResponse(RetrieveTask task) {
        TextView textView = (TextView) findViewById(R.id.ChatWindow);
        textView.setText("-----\n");
        PriorityQueue<Message> queue = task.result;
        if(queue == null) {
            textView.setText("no messages could be retrieved");
        } else {
            while(queue.peek() != null) {
                textView.append(queue.poll().text + "\n-----\n");
            }
        }
    }


    private void deregisterCallback(SendAndReceiveTask task) {
        Toast.makeText(getApplicationContext(), getString(R.string.deregistered), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Helper.deregister(username, uuid,this);
        super.onBackPressed();
    }
}
