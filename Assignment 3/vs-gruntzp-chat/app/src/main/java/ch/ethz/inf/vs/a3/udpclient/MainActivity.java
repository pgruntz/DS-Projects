package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;

import ch.ethz.inf.vs.a2.gruntzp.vsgruntzpchat.R;
import ch.ethz.inf.vs.a3.message.MessageTypes;

public class MainActivity extends AppCompatActivity {


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
                Snackbar.make(view, "Connecting ..", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                EditText txtUserName = (EditText) findViewById(R.id.txtUserName);
                if (tryRegister(txtUserName.getText().toString()))
                    Toast.makeText(getApplicationContext(), "registered", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Boolean tryRegister(String username)
    {
        boolean success = false;
        for (int i = 0; i < 5; i++)
            if (success = Register(username)) break;
        return success;
    }

    private Boolean Register(final String username) {

        try {

            //building the socket
            DatagramSocket socket = new DatagramSocket(NetworkConsts.UDP_PORT);
            socket.setSoTimeout(NetworkConsts.SOCKET_TIMEOUT);
            InetAddress address = InetAddress.getByName(NetworkConsts.SERVER_ADDRESS);

            // building the message
            UUID uuid = UUID.randomUUID();
            String message = Helper.JSONmessage(uuid.toString(), username, MessageTypes.REGISTER);
            byte[] sendBuf = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, NetworkConsts.UDP_PORT);

            //preparing response message
            byte[] recBuf = new byte[256];
            DatagramPacket recPacket = new DatagramPacket(recBuf, recBuf.length);

            //Sending, receiving
            Helper.NetworkRunnable r = new Helper.NetworkRunnable(socket, sendPacket, recPacket);

            AsyncTask.execute(r);
            socket.close();

            // success?
            if (r.recP == null)
                return false;

            // parsing response
            String received = new String(r.recP.getData(), 0, r.recP.getLength());
            Log.d("received", received);

            JSONObject o = new JSONObject(received);
            String type = o.getString("type");
            if (type.equals(MessageTypes.ACK_MESSAGE))
                return true;



        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

}
