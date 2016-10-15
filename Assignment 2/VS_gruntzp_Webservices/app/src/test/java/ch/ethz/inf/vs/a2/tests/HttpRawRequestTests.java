package ch.ethz.inf.vs.a2.tests;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.ethz.inf.vs.a2.http.HttpRawRequest;
import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.HttpRawRequestImpl;


public class HttpRawRequestTests{

	private final int TEST_PORT = 1234;
	private final String TEST_HOST = "somehost.tld";
	private final String TEST_PATH = "/some/path";
	private final String HTTP_VERSION = "1.1";
	
	@Test
	public void getMethod() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("^([A-Z]+) (?:.*)");
		Matcher matcher = pattern.matcher(request);

		Assert.assertTrue("Uppercase HTTP method expected.", matcher.find());
		String method = matcher.group(1);
		Assert.assertTrue("GET method expected, but \"" + method + "\" found.", method.equals("GET"));
	}
	
	@Test
	public void pathFormat() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("^(?:.*) (/|(?:/\\w+)+) (?:.*)");
		Matcher matcher = pattern.matcher(request);
		Assert.assertTrue("Unexpected Request-URI format.", matcher.find());
		Assert.assertTrue("Request-URI expected as abs_path.", matcher.group(1).equals(TEST_PATH));
	}
	
	@Test
	public void httpVersion() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("^(?:(?:.*) ){2}(HTTP/([0-9]\\.[0-9]))");
		Matcher matcher = pattern.matcher(request);
		matcher.find();
		Assert.assertFalse("HTTP version of format \"HTTP/x.x\" expected.", matcher.group(1).isEmpty());
		String version = matcher.group(2);
		Assert.assertTrue("HTTP version " + HTTP_VERSION + " expected, but \"" + version + "\" found.", version.equals(HTTP_VERSION));
	}
	
	@Test
	public void lineEndings() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("(\\r\\n)");
		String[] split = pattern.split(request);
		Assert.assertTrue("Lines must end with CRLF.", split.length > 0);
	}
	
	@Test
	public void noLF() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("(?<!\\r)(\\n)(?!\\r)");
		Matcher matcher = pattern.matcher(request);
		Assert.assertFalse("Lines must not end with LF.", matcher.find());

		pattern = Pattern.compile("(?<!\\n)(\\r)(?!\\n)");
		matcher = pattern.matcher(request);
		Assert.assertFalse("Lines must not end with CR.", matcher.find());
		
		pattern = Pattern.compile("(?<!\\r)(\\n\\r)(?!\\n)");
		matcher = pattern.matcher(request);
		Assert.assertFalse("Lines must not end with LFCR.", matcher.find());
		
		pattern = Pattern.compile("\\r\\n\\r\\n$");
		matcher = pattern.matcher(request);
		Assert.assertTrue("Header section must be separated from body with additional CRLF.", matcher.find());
	}
	
	@Test
	public void headerFields() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("(\\r\\n)");
		String[] split = pattern.split(request);
		pattern = Pattern.compile("([a-zA-Z\\-]+)(?:\\: )(.+)$");
		for (int i = 1; i < split.length; i++) {
			Matcher matcher = pattern.matcher(split[i]);
			Assert.assertTrue("Unexpected header format for \"" + split[i] + "\".", matcher.find());
			Assert.assertFalse("Header field name expected, but not found in \"" + split[i] + "\".", matcher.group(1).isEmpty());
			Assert.assertFalse("Header field value expected, but not found in \"" + split[i] + "\".", matcher.group(2).isEmpty());
		}
	}
	
	@Test
	public void hostHeader() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("(\\r\\n)");
		String[] split = pattern.split(request);
		pattern = Pattern.compile("([a-zA-Z\\-]+)(?:\\: )(.+)$");
		boolean found = false;
		for (int i = 1; i < split.length; i++) {
			Matcher matcher = pattern.matcher(split[i]);
			Assert.assertTrue("Unexpected header format for \"" + split[i] + "\".", matcher.find());
			if (matcher.group(1).equals("Host")) {
				found = true;
				String host = matcher.group(2);
				Assert.assertTrue("\"" + host + "\" does not match expected host format host:port.", host.equals(TEST_HOST + ":" + TEST_PORT));
				break;
			}
		}
		Assert.assertTrue("Header field \"Host\" expected, but not found.", found);
	}
	
	@Test
	public void connectionHeader() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("(\\r\\n)");
		String[] split = pattern.split(request);
		pattern = Pattern.compile("([a-zA-Z\\-]+)(?:\\: )(.+)$");
		boolean found = false;
		for (int i = 1; i < split.length; i++) {
			Matcher matcher = pattern.matcher(split[i]);
			Assert.assertTrue("Unexpected header format for \"" + split[i] + "\".", matcher.find());
			if (matcher.group(1).equals("Connection")) {
				found = true;
				String connection = matcher.group(2);
				Assert.assertTrue("\"" + connection + "\" does not match expected value \"close\".", connection.equals("close"));
				break;
			}
		}
		Assert.assertTrue("Header field \"Connection\" expected, but not found.", found);
	}
	
	@Test
	public void acceptHeader() {
		String request = createRequest();
		Pattern pattern = Pattern.compile("(\\r\\n)");
		String[] split = pattern.split(request);
		pattern = Pattern.compile("([a-zA-Z\\-]+)(?:\\: )(.+)$");
		boolean found = false;
		for (int i = 1; i < split.length; i++) {
			Matcher matcher = pattern.matcher(split[i]);
			Assert.assertTrue("Unexpected header format for \"" + split[i] + "\".", matcher.find());
			if (matcher.group(1).equals("Accept")) {
				found = true;
				String accept = matcher.group(2);
				Assert.assertTrue("\"" + accept + "\" does not match expected value \"text/html\".", accept.equals("text/html"));
				break;
			}
		}
		Assert.assertTrue("Header field \"Accept\" expected, but not found.", found);;
	}
	
	private String createRequest(){
		HttpRawRequest request = new HttpRawRequestImpl();
		return request.generateRequest(TEST_HOST, TEST_PORT, TEST_PATH);
	}

}
