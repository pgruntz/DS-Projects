package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import static android.content.ContentValues.TAG;

/**
 * Created by Patrick on 10/20/2016.
 */

public class ServerService extends Service{

    private int port = 8080;
    private ServerSocket server;
    //private String ip = ServerAcitivity.getLocalIpAddress()+":8080"; //for wlan
    private String ip = "127.0.0.1:1234"; //for local emulator


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable socketStart;
    private Socket socket;

    {
        socketStart = new Runnable() {
            public void run() {
                try {
                    server = new ServerSocket(port);
                    while (true) {
                        socket = server.accept();
                        Runnable conn = new Connection(socket, ip, sSensors);
                        new Thread(conn).start();
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        };
    }
    private ServerSensors sSensors;// = new ServerSensors();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorManager sensorM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sSensors = new ServerSensors(sensorM);
        new Thread(socketStart).start();

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(server!=null) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sSensors.unregisterListener();
        Toast.makeText(this, "Service and Server closed", Toast.LENGTH_LONG).show();
    }




}
