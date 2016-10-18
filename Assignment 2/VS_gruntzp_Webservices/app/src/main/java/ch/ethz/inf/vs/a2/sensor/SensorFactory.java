package ch.ethz.inf.vs.a2.sensor;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.JSonSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.TextSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.XmlSensor;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.RawHttpSensor;


public abstract class SensorFactory {
	public static Sensor getInstance(Type type) {
		switch (type) {
		case RAW_HTTP:
			// return Sensor implementation using a raw HTTP request
			return new RawHttpSensor();
		case TEXT:
			// return Sensor implementation using text/html representation
			return new TextSensor();
		case JSON:
			// return Sensor implementation using application/json representation
			return new JSonSensor();
		case XML:
			// return Sensor implementation using application/xml representation
			return new XmlSensor();
		case SOAP:
			// return Sensor implementation using a SOAPObject
			//return new SoapSensor();
		default:
			return null;
		}
	}

	public enum Type {
		RAW_HTTP, TEXT, JSON, XML, SOAP;
	}
}
