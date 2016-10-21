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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Patrick on 10/20/2016.
 */

public class ServerService extends Service {

    private int port = 8080;
    private ServerSocket server;

    private String rootHTML = "HTTP/1.1 200 OK\r\n\r\n"+
            "<html><body><h1>Phone Webservices - Root</h1>" +
            "<ul><li><a id=\"home\" href=\"http://127.0.0.1:1234/\">Home</a></li>" +
            "<ul><li><a href = \"/sensors\">Sensors</a></li><li><a href = \"/actuators\">Actuators</a></li></ul><br><br><br></body><html>";
    private String sensorsHTML = "HTTP/1.1 200 OK\r\n\r\n"+
            "<html><body><h1>Sensors</h1>" +
            "<ul><li><a id=\"home\" href=\"http://127.0.0.1:1234/\">Home</a></li>" +
            "<ul><li><a href = \"/sensors/sensor1\">Sensor 1</a></li><li><a href = \"/sensors/sensor2\">Sensor 2</a></li></ul><br><br><br></body><html>";
    private String actuatorsHTML = "HTTP/1.1 200 OK\r\n\r\n"+
            "<html><body><h1>Actuators</h1>" +
            "<ul><li><a id=\"home\" href=\"http://127.0.0.1:1234/\">Home</a></li>" +
            "<ul><li><a href = \"/actuators/actuator1\">Actuator 1</a></li><li><a href = \"/actuators/actuator2\">Actuator 2</a></li></ul><br><br><br></body><html>";
    private String sensor1HTML = "HTTP/1.1 200 OK\r\n\r\n"+
            "<html><body><h1>Sensor 1</h1>" +
            "<ul><li><a id=\"home\" href=\"http://127.0.0.1:1234/\">Home</a></li></ul><br><br><br></body><html>";
    private String sensor2HTML = "HTTP/1.1 200 OK\r\n\r\n"+
            "<html><body><h1>Sensor 2</h1>" +
            "<ul><li><a id=\"home\" href=\"http://127.0.0.1:1234/\">Home</a></li></ul><br><br><br></body><html>";
    private String actuator1HTML = "HTTP/1.1 200 OK\r\n\r\n"+
            "<html><body><h1>Actuator 1</h1>" +
            "<ul><li><a id=\"home\" href=\"http://127.0.0.1:1234/\">Home</a></li></ul><br><br><br></body><html>";
    private String actuator2HTML = "HTTP/1.1 200 OK\r\n\r\n"+
            "<html><body><h1>Actuator 2</h1>" +
            "<ul><li><a id=\"home\" href=\"http://127.0.0.1:1234/\">Home</a></li></ul><br><br><br></body><html>";


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
                        PrintWriter out = new PrintWriter(socket.getOutputStream());

                        String request = in.readLine();
                        String requestArr[] = request.split(" ", 3);

                        String method = requestArr[0];   //the
                        String path = requestArr[1];
                        if (method.equals("GET")){
                            if(path.equals("/"))
                                out.println(rootHTML);
                            if(path.equals("/sensors"))
                                out.println(sensorsHTML);
                            if(path.equals("/actuators"))
                                out.println(actuatorsHTML);
                            if(path.equals("/sensors/sensor1"))
                                out.println(sensor1HTML);
                            if(path.equals("/sensors/sensor2"))
                                out.println(sensor2HTML);
                            if(path.equals("/actuators/actuator1"))
                                out.println(actuator1HTML);
                            if(path.equals("/actuators/actuator2"))
                                out.println(actuator2HTML);
                        }
                        //StringBuilder sb = new StringBuilder();
                        //String rs = in.readLine();
                        //while (rs!=null && rs.length() != 0) {
                        //    sb.append(rs + "\n");
                        //    rs = in.readLine();
                        //}
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        out.println(dateFormat.format(new Date()));


                        //String request = sb.toString();
                        //Intent broadcastIntent = new Intent();
                        //broadcastIntent.putExtra("request", request);
                        //broadcastIntent.setAction("REQUEST");
                        //sendBroadcast(broadcastIntent);

                        //String send = "HTTP/1.1 200 OK\nserver: grizzly/1.9.18\nContent-Type: text/html\nDate: Thu, 20 Oct 2016 16:23:59 GMT\nConnection: close\n\n<html>\n <body>\n    <h1>Test Content</h1>\n <p>Testing, testing, testing...</p>\n   </body>\n</html>";
                        //String send = "HTTP/1.1 200 OK\r\n\r\n<html><body><h1>Test Content</h1><p>Testing,testing, testing...</p></body></html>";
                        //PrintWriter out = new PrintWriter(socket.getOutputStream());
                        //out.print(rootHTML);
                        out.flush();
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
