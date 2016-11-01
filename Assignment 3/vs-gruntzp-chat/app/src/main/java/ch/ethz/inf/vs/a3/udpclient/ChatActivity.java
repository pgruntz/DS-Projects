package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.UUID;

import ch.ethz.inf.vs.a2.gruntzp.vsgruntzpchat.R;
import ch.ethz.inf.vs.a3.message.MessageTypes;

public class ChatActivity extends AppCompatActivity implements SendAndReceiveTask.ResponseHandler {

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

    @Override
    public void HandleResponse(SendAndReceiveTask task, Object... passthrough) {
        if ((int) task.passthrough[0] == Helper.DEREGISTER_REQUEST)
            deregisterCallback(task);
        else
            throw new UnknownError();
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
