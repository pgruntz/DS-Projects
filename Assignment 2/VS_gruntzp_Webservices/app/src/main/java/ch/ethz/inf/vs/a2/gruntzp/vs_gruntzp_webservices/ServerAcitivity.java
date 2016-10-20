package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ServerAcitivity extends AppCompatActivity {

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent recIntent) {
                recIntent.getExtras();
                System.out.println("received");
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("REQUEST");
        registerReceiver(receiver, filter);

        String IP = getLocalIpAddress();
        ((TextView) findViewById(R.id.txtIP)).setText(IP);
    }

    private String getLocalIpAddress() {
        String address= null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName() == "wlan0")
                {

                }
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    address = new String(inetAddress.getHostAddress().toString());
                    if (!inetAddress.isLoopbackAddress() && address.length() < 18) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            String msg = ex.getMessage();
            //do nothing
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
    public void onClickToggle(View v){
        ToggleButton t = (ToggleButton) v;
        if (t.isChecked()) {
            Intent service = new Intent(this, ServerService.class);
            startService(service);
        } else {
            stopService(new Intent(this, ServerService.class));
        }
    }

}

