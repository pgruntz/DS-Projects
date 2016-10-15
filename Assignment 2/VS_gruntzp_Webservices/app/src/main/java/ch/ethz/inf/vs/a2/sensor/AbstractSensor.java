package ch.ethz.inf.vs.a2.sensor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

/**
 * Implementation of a sensor representation.
 *
 * @author Leyna Sadamori
 * @see Sensor
 * @see SensorListener
 */
public abstract class AbstractSensor implements Sensor {
    protected List<SensorListener> listeners = new ArrayList<SensorListener>();

    @Override
    public void registerListener(SensorListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(SensorListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void getTemperature() {
        new AsyncWorker().execute();
    }

    /**
     * Send message to all listeners. Useful for error messages.
     *
     * @param message Message to be sent to listeners
     */
    protected void sendMessage(String message) {
        for (SensorListener listener : listeners) {
            listener.onReceiveMessage(message);
        }
    }

    /**
     * Send parsed sensor values to all listeners.
     *
     * @param value Sensor value to be sent to listeners
     */
    protected void sendValue(double value) {
        for (SensorListener listener : listeners) {
            listener.onReceiveSensorValue(value);
        }
    }

    /**
     * AsyncTask to execute the request in a separate thread.
     * The response is parsed by {@link Sensor#parseResponse(String)}
     * and the value is sent to the listeners.
     * Exceptions are caught and sent as a message to the listeners.
     *
     * @author Leyna Sadamori
     */
    public class AsyncWorker extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                return executeRequest();

            } catch (Exception e) {
                e.printStackTrace();
                publishProgress(e.toString());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            sendMessage(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                sendValue(parseResponse(result));
        }
    }
}
