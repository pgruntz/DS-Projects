package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patrick on 10/23/2016.
 */

public class Connection extends Service implements Runnable{
    private Socket socket;
    private String ip;
    private ServerSensors sSensors;

    public Connection(Socket socket, String ip, ServerSensors sSensors){
        this.socket=socket;
        this.ip = ip;
        this.sSensors = sSensors;
    }

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

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String request = in.readLine();
            String requestArr[] = request.split(" ", 3);

            String method = requestArr[0];
            String path = requestArr[1];
            if (method.equals("GET")) {
                if (path.equals("/"))
                    out.println(rootHTML());
                else if (path.equals("/sensors"))
                    out.println(sensorsHTML());
                else if (path.equals("/actuators"))
                    out.println(actuatorsHTML());
                else if (path.equals("/sensors/sensor1"))
                    out.println(sensor1HTML());
                else if (path.equals("/sensors/sensor2"))
                    out.println(sensor2HTML());
                else if (path.equals("/actuators/actuator1"))
                    out.println(actuator1HTML());
                else if (path.equals("/actuators/actuator2"))
                    out.println(actuator2HTML());
                else
                    out.println("ERROR 404 - page not found");


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
                else if(path.equals("/actuators/actuator2")){
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
                }else
                    out.println("ERROR 404 - page not found");
            }else{
                out.println("ERROR 405 - method not allowed");
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            out.println(dateFormat.format(new Date()));

            out.flush();
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}
}
