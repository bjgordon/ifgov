import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ifgov.server.DataRequester;

public class DataRequesterTest {

	@Test
	public void get() throws Exception {
		// HttpGet httpget = new HttpGet(
		// "http://ec2-54-79-39-36.ap-southeast-2.compute.amazonaws.com:8080/cgi-bin/WMSsource.py?sourceid=1&lat=1.0&lon=2.0");
		//
		// // java.net.URI uri = new URIBuilder()
		// // .setScheme("http")
		// // .setHost(
		// // "ec2-54-79-39-36.ap-southeast-2.compute.amazonaws.com:8080")
		// // .setPath("/cgi-bin/WMSsource.py").setParameter("sourceid", "1")
		// // .setParameter("lat", "1.0").setParameter("lon", "2.0").build();
		// // HttpGet httpget = new HttpGet(uri);
		// System.out.println(httpget.getURI());
		//
		// CloseableHttpClient httpclient = HttpClients.createDefault();
		// // HttpGet httpget = new HttpGet("http://localhost/");
		// CloseableHttpResponse response = httpclient.execute(httpget);
		// try {
		// System.out.println(response.getEntity().toString());
		// } finally {
		// response.close();
		// }

		//
		// Request.Get(
		// "http://ec2-54-79-39-36.ap-southeast-2.compute.amazonaws.com:8080/cgi-bin/WMSsource.py?sourceid=1&lat=1.0&lon=2.0")
		// .execute().returnContent();
		// String url =
		// "http://ec2-54-79-39-36.ap-southeast-2.compute.amazonaws.com:8080/cgi-bin/WMSsource.py?sourceid=1&lat=1.0&lon=2.0";
		// URL obj = new URL(url);
		// HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		//
		// int responseCode = con.getResponseCode();
		// System.out.println("\nSending 'GET' request to URL : " + url);
		// System.out.println("Response Code : " + responseCode);
		//
		// BufferedReader in = new BufferedReader(new InputStreamReader(
		// con.getInputStream()));
		// String inputLine;
		// StringBuffer response = new StringBuffer();
		//
		// while ((inputLine = in.readLine()) != null) {
		// response.append(inputLine);
		// }
		// in.close();
		//
		// System.out.println(response);

		String r = DataRequester.getBroadband(1.0, 2.0);
		// should be no data here
		assertEquals("{}", r);
	}
}
