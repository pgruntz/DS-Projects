package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
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
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Patrick on 10/20/2016.
 */

public class ServerService extends Service{

    private int port = 8080;
    private ServerSocket server;
    private String ip = ServerAcitivity.getLocalIpAddress()+":8080";

    private String rootHTML(){
        return "HTTP/1.1 200 OK\r\n\r\n"+
            "<html><body><h1>Phone Webservices - Root</h1>" +
            "<ul><li><a id=\"home\" href=\"http://" + ip +"\">Home</a></li>" +
            "<ul><li><a href = \"/sensors\">Sensors</a></li><li><a href = \"/actuators\">Actuators</a></li></ul><br><br><br></body><html>";
    }
    private String sensorsHTML(){
        return "HTTP/1.1 200 OK\r\n\r\n"+
                "<html><body><h1>Sensors</h1>" +
                "<ul><li><a id=\"home\" href=\"http://" + ip + "\">Home</a></li>" +
                "<ul><li><a href = \"/sensors/sensor1\">Light</a></li><li><a href = \"/sensors/sensor2\">Temperature</a></li></ul><br><br><br></body><html>";
    }
    private String actuatorsHTML() {
        return "HTTP/1.1 200 OK\r\n\r\n" +
                "<html><body><h1>Actuators</h1>" +
                "<ul><li><a id=\"home\" href=\"http://" + ip + "\">Home</a></li>" +
                "<ul><li><a href = \"/actuators/actuator1\">Vibrate</a></li><li><a href = \"/actuators/actuator2\">Brightness</a></li></ul><br><br><br></body><html>";
    }
    private String sensor1HTML(){
        return "HTTP/1.1 200 OK\r\n\r\n"+
                "<html><body><h1>Light Sensor<br><br>Value: " + sSensors.getSensorValue(1) + "</h1>" +
                "<ul><li><a id=\"home\" href=\"http://" + ip + "\">Home</a></li></ul><br><br><br></body><html>";
    };
    private String sensor2HTML(){
        return "HTTP/1.1 200 OK\r\n\r\n"+
                "<html><body><h1>Temperature Sensor<br><br>Value: " + sSensors.getSensorValue(2) + "</h1>" +
                "<ul><li><a id=\"home\" href=\"http://" + ip + "\">Home</a></li></ul><br><br><br></body><html>";
    }
    private String actuator1HTML(){
        return "HTTP/1.1 200 OK\r\n\r\n"+
                "<html><body><h1>Vibrate</h1><br>" +
                "<form method=\"post\" action=\"/actuators/actuator1\">" +
                "<input id=\"test\" type=\"radio\" name=\"actuator\" value=\"on\" checked>On<br>"+
                "<input type=\"radio\" name=\"actuator\" value=\"off\">Off<br>" +
                "<input type=\"submit\" value=\"Set\"></form>" +
                "<ul><li><a id=\"home\" href=\"http://" + ip + "\">Home</a></li></ul><br><br><br></body><html>";
    }
    private String actuator2HTML(){
        return "HTTP/1.1 200 OK\r\n\r\n"+
                "<html><body><h1>Set Screen Brightness</h1><br>" +
                "<form method=\"post\" action=\"/actuators/actuator2\">" +
                "<input id=\"test\" type=\"radio\" name=\"actuator\" value=\"dark\" checked>dark<br>"+
                "<input type=\"radio\" name=\"actuator\" value=\"bright\">bright<br>" +
                "<input type=\"radio\" name=\"actuator\" value=\"automatic\">automatic<br>" +
                "<input type=\"submit\" value=\"Set\"></form>" +
                "<ul><li><a id=\"home\" href=\"http://" + ip + "\">Home</a></li></ul><br><br><br></body><html>";
    }


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

                        String method = requestArr[0];
                        String path = requestArr[1];
                        if (method.equals("GET")) {
                            if (path.equals("/"))
                                out.println(rootHTML());
                            if (path.equals("/sensors"))
                                out.println(sensorsHTML());
                            if (path.equals("/actuators"))
                                out.println(actuatorsHTML());
                            if (path.equals("/sensors/sensor1"))
                                out.println(sensor1HTML());
                            if (path.equals("/sensors/sensor2"))
                                out.println(sensor2HTML());
                            if (path.equals("/actuators/actuator1"))
                                out.println(actuator1HTML());
                            if (path.equals("/actuators/actuator2"))
                                out.println(actuator2HTML());


                        } else if (method.equals("POST")) {
                            Map<String,String> map = new HashMap<>();
                            StringBuilder sb = new StringBuilder();

                            while (!request.equals("")) {
                                //out.println(request);
                                sb.append(request + "\n");
                                if(request.contains(":")){
                                    int i = request.indexOf(":");
                                    map.put(request.substring(0,i),request.substring(i+2));
                                }
                                request = in.readLine();
                            }
                            int length = Integer.parseInt(map.get("Content-Length"));
                            int off = 0;
                            char[] cbuf = new char[length];
                            while(off < length) {
                                int res = in.read(cbuf, off,  length-off);
                                off += res;
                            }
                            String body = new String(cbuf);

                            if(path.equals("/actuators/actuator1")){
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                out.println(actuator1HTML());
                                int i = body.indexOf("=");
                                if(body.substring(i+1).equals("on")){
                                    v.vibrate(20000);
                                    out.println("Phone vibrating, ");
                                }else if(body.substring(i+1).equals("off")){
                                    v.cancel();
                                    out.println("Phone stops vibrating, ");
                                }
                            }
                            if(path.equals("/actuators/actuator2")){
                                out.println(actuator2HTML());
                                int i = body.indexOf("=");
                                if(body.substring(i+1).equals("automatic")){
                                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                                    out.println("Brighntness set to automatic, ");
                                }else if(body.substring(i+1).equals("dark")){
                                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 10);
                                    out.println("Brighntness set to dark, ");
                                }else if (body.substring(i+1).equals("bright")){
                                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 200);
                                    out.println("Brighntness set to bright, ");
                                }else{
                                    out.println("No Brightness set, ");
                                }
                            }
                        }

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        out.println(dateFormat.format(new Date()));

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

    private ServerSensors sSensors;// = new ServerSensors();

    private String sensor1Val;// = sSensors.getSensorValue(1);
    private String sensor2Val;// = sSensors.getSensorValue(2);
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorManager sensorM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sSensors = new ServerSensors(sensorM);
        sensor1Val = sSensors.getSensorValue(1);
        sensor2Val = sSensors.getSensorValue(2);
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
        sSensors.unregisterListener();
        Toast.makeText(this, "Service and Server closed", Toast.LENGTH_LONG).show();
    }




}
