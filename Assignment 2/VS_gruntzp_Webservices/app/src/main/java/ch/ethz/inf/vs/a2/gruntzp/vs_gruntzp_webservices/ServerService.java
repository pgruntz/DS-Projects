package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static android.content.ContentValues.TAG;

/**
 * Created by Patrick on 10/20/2016.
 */

public class ServerService extends Service {

    private int port = 8080;
    private ServerSocket server;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable conn;

    {
        conn = new Runnable() {
            public void run() {
                try {
                    server = new ServerSocket(port);

                    while (true) {
                        Socket socket = server.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        StringBuilder sb = new StringBuilder();
                        String rs = in.readLine();
                        while (rs!=null && rs.length() != 0) {
                            sb.append(rs + "\n");
                            rs = in.readLine();
                        }


                            String request = sb.toString();
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.putExtra("request", request);
                            broadcastIntent.setAction("REQUEST");
                            sendBroadcast(broadcastIntent);



                        //String send = "HTTP/1.1 200 OK\nserver: grizzly/1.9.18\nContent-Type: text/html\nDate: Thu, 20 Oct 2016 16:23:59 GMT\nConnection: close\n\n<html>\n <body>\n    <h1>Test Content</h1>\n <p>Testing, testing, testing...</p>\n   </body>\n</html>";
                        String send = "HTTP/1.1 200 OK\r\n\r\n<html><body><h1>Test Content</h1><p>Testing,testing, testing...</p></body></html>";
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                        out.println(send);
                        out.flush();
                        Thread.sleep(1000);
                        out.close();
                        in.close();
                        socket.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }


            }
        };
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(conn).start();
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
        Toast.makeText(this, "Service and Server closed", Toast.LENGTH_LONG).show();
    }


}
