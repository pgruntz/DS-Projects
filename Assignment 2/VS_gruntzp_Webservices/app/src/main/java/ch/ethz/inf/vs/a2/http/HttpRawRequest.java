package ch.ethz.inf.vs.a2.http;


/**
 * 
 * @author Leyna Sadamori
 * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616.html" target="_blank">RFC2616</a> for the HTTP/1.1 specification.
 *
 */
public interface HttpRawRequest {

	/**
	 * Generate a simple HTTP GET request of HTTP version 1.1.
	 * The request should at least set the optional header field "Connection" to "close".
	 *
	 * @param host Host address
	 * @param port Destination port
	 * @param path Absolute path of the requested resource
	 *             (See <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1.2" target="_blank">Request-URI</a>)
	 *
	 * @return HTTP GET request
	 */
	public String generateRequest(String host, int port, String path);
}
