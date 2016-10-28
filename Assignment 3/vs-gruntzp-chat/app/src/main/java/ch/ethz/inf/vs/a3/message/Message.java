package ch.ethz.inf.vs.a3.message;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.a3.clock.VectorClock;

/**
 * Created by niederbm on 10/28/16.
 */

public class Message {

    public VectorClock timestamp;
    public String text;

    private static final String MESSAGE = "message";


    public Message(JSONObject o) throws MessageCreationException{
        try{
            String type = o.getJSONObject("header").getString("type");
            if(!(type.equals(MESSAGE))){
                throw new MessageCreationException(MessageCreationException.NO_MSG);
            } else {
                this.timestamp = new VectorClock();
                this.timestamp.setClockFromString(o.getJSONObject("header").getString("timestamp"));
                this.text = o.getJSONObject("body").getString("content");
            }
        } catch(JSONException e) {
            throw new MessageCreationException(MessageCreationException.FAULTY_JSON);
        }
    }


    public class MessageCreationException extends RuntimeException{
        public static final String NO_MSG = "NO_MESSAGE";
        public static final String FAULTY_JSON = "FAULTY_JSON";
        public MessageCreationException(String error){
            super(error);
        }
    }
}
