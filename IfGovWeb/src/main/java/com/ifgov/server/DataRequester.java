package com.ifgov.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get data from the datasources. A python script running on our server will
 * handle connecting to the data, and returning the result in JSON.
 */
public class DataRequester {
	protected final static Logger logger = LoggerFactory
			.getLogger(DataRequester.class);

	public static String getBroadband(double lat, double lon) throws Exception {
		final int sourceid = 1; // broadband wms source id
		// todo build url better
		String url = "http://ec2-54-79-39-36.ap-southeast-2.compute.amazonaws.com:8080/cgi-bin/WMSsource.py?sourceid="
				+ sourceid + "&lat=" + lat + "&lon=" + lon;
		URL obj = new URL(url);

		// grr why does this error?
		// java.net.URI uri = new URIBuilder()
		// .setScheme("http")
		// .setHost(
		// "ec2-54-79-39-36.ap-southeast-2.compute.amazonaws.com:8080")
		// .setPath("/cgi-bin/WMSsource.py").setParameter("sourceid", "1")
		// .setParameter("lat", "" + lat).setParameter("lon", "" + lon)
		// .build();
		// System.out.println(uri);

		logger.debug("GET: " + url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		int responseCode = con.getResponseCode();

		logger.debug("Response code " + responseCode);
		if (responseCode != 200) {
			throw new RuntimeException("Response code not OK: " + responseCode);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response);
		return response.toString();

		// CloseableHttpClient httpclient = HttpClients.createDefault();
		//
		// HttpGet httpget = new HttpGet("http://localhost/");
		// CloseableHttpResponse response = httpclient.execute(httpget);
		// try {
		// System.out.println(response.getEntity().toString());
		// return response.getEntity().toString();
		// } finally {
		// response.close();
		// }

	}
}
