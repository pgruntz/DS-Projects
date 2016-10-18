package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ServerAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

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
}

