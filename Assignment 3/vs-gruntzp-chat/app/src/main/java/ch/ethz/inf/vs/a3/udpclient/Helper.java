package ch.ethz.inf.vs.a3.udpclient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PhiSc on 25.10.2016.
 */

public class Helper {
    public static String JSONmessage(String uuid, String username, JSONObject timestamp, String type, JSONObject Body) throws JSONException {
        JSONObject o = new JSONObject();

        JSONObject header = new JSONObject();
        header.put("username", username);
        header.put("uuid", uuid);
        header.put("timestamp", timestamp);
        header.put("type", type);

        o.put("header", header);
        o.put("body", Body);
        return o.toString(2);
    }

    public static String JSONmessage(String uuid, String username, String type) throws JSONException {
        return JSONmessage(uuid, username, new JSONObject(), type, new JSONObject());
    }
}
