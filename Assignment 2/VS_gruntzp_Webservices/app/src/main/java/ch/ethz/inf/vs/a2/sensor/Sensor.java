package ch.ethz.inf.vs.a2.sensor;

/**
 * A sensor representation that provides temperature measurements. 
 * 
 * @author Leyna Sadamori
 *
 */
public interface Sensor {
	
	/**
	 * Executes a request to fetch the temperature value.
	 *
	 * @throws Exception Any exception that could happen during the request
	 *
	 * @return Response (not parsed yet)
	 */
	public String executeRequest() throws Exception;

	/**
	 * Parse response that has been returned after sending the request.
	 *
	 * @param response Raw response
	 *
	 * @return Parsed sensor value or {@link Double#NaN} if parsing fails
     */
	public double parseResponse(String response);

	/**
	 * Invoke request to fetch temperature value.
	 */
	public void getTemperature();


	/**
	 * Register a SensorListener to this sensor.
	 * @param listener The SensorListener to be registered
	 */
	public void registerListener(SensorListener listener);

	/**
	 * Unregister a SensorListener from this sensor.
	 * @param listener The SensorListener to be unregistered
	 */
	public void unregisterListener(SensorListener listener);
}
