package ch.ethz.inf.vs.a3.udpclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import ch.ethz.inf.vs.a2.gruntzp.vsgruntzpchat.R;
import ch.ethz.inf.vs.a3.message.MessageTypes;

public class MainActivity extends AppCompatActivity implements SendAndReceiveTask.ResponseHandler {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);



    }

    public void onRegisterClicked(View view)
    {
        EditText txtUserName = (EditText) findViewById(R.id.txtUserName);
        String username = txtUserName.getText().toString();

        if (username.length() == 0)
            Toast.makeText(getApplicationContext(), getString(R.string.usernameTooShort), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), getString(R.string.tryRegister), Toast.LENGTH_SHORT).show();
        tryRegister(username, 5);
    }

    private void tryRegister(String username, int attempts)
    {
        // everything after 'this' will be be available in 'HandleResponse'
        SendAndReceiveTask t = new SendAndReceiveTask(this, Helper.REGISTER_REQUEST, attempts, username);
        try {
            String message = Helper.JSONmessage(username, MessageTypes.REGISTER);
            //first parameter is message to send, second is either this constant or a real uuid.
            t.execute(message, SendAndReceiveTask.RANOM_UUID);
        } catch (JSONException e) {
            e.printStackTrace();
            registerFailed();
        }

    }

    //deregister with only one attempt
    private void deregister(String username, String uuid)
    {
        SendAndReceiveTask t = new SendAndReceiveTask(this, Helper.DEREGISTER_REQUEST);
        try {
            String message = Helper.JSONmessage(uuid, username, MessageTypes.DEREGISTER);
            t.execute(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void HandleResponse(SendAndReceiveTask task, Object... passthrough) {
        switch ((int) task.passthrough[0]){
            case Helper.REGISTER_REQUEST:
                RegisterCallbback(
                        task,
                        (int) task.passthrough[1],
                        (String) task.passthrough[2]
                        );
                break;
            case Helper.DEREGISTER_REQUEST:
                deregisterCallback(task);
                break;
            default:
                throw new UnknownError();
        }

    }

    private void RegisterCallbback(SendAndReceiveTask task, int attempts, String username)
    {
        try {
            //JSON response arrived
            if (task.result.getJSONObject("header").getString("type").equals(MessageTypes.ACK_MESSAGE)) {
                Toast.makeText(getApplicationContext(), getString(R.string.doneReigster), Toast.LENGTH_SHORT).show();

                //TODO: (@Matthias) Intent to other Activity;
                String uuid = task.usedUUID;
                deregister(username, uuid);

                //Necessary!!
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (attempts > 1) {
            tryRegister(username, attempts - 1);
        } else {
            registerFailed();
        }
    }

    private void registerFailed() {
        Toast.makeText(getApplicationContext(), getString(R.string.failedRegister), Toast.LENGTH_LONG).show();
    }

    private void deregisterCallback(SendAndReceiveTask task) {
        Toast.makeText(getApplicationContext(), getString(R.string.deregistered), Toast.LENGTH_SHORT).show();
    }


    /// Müll ///

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
