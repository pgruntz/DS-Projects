package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.tryRegister), Toast.LENGTH_SHORT).show();
                EditText txtUserName = (EditText) findViewById(R.id.txtUserName);
                String username = txtUserName.getText().toString();
                tryRegister(username, 5);
            }
        });

    }

    private void tryRegister(String username, int attempts)
    {
        SendAndReceiveTask t = new SendAndReceiveTask(this, attempts, username);
        t.execute(username, "");
    }

    @Override
    public void HandleResponse(SendAndReceiveTask task) {
        int attempts = (int) task.passthrough[0];
        String username = (String) task.passthrough[1];
        Boolean success = false;

        try {
            //JSON response arrived
            if (task.result.getJSONObject("header").getString("type").equals(MessageTypes.ACK_MESSAGE)) {
                success = true;
                Toast.makeText(getApplicationContext(), getString(R.string.doneReigster), Toast.LENGTH_SHORT).show();

                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (attempts > 1) {
            tryRegister(username, attempts - 1);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.failedRegister), Toast.LENGTH_LONG).show();
        }
    }


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
