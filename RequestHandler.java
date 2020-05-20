import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class RequestHandler {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	// Sending data via a GET request to the Flask server
	private void sendData() throws IOException {
		// End point to which the data will be sent
		URL iris = new URL(
				"http://127.0.0.1:5000/predict?sepal_length=7.0&sepal_width=2.5&petal_length=7.5&petal_width=2");
		URLConnection connection = iris.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			System.out.println(inputLine);
		in.close();
	}

	
	
	// Sending data via a POST request to the Flask server
	private String sendPostData() throws IOException {
		URL url = null;
		try {
			// End point to which the data will be sent
			url = new URL("http://127.0.0.1:5000/predict_iris");
		} catch (MalformedURLException e) {
			log.error("Incorrect url endpoint");
		}

		Map<String, Object> params = new HashMap<>();
		params.put("sepal_length", 7);
		params.put("sepal_width", 2.5);
		params.put("petal_length", 7);
		params.put("petal_width", 2);
		

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;
		log.info("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
			return output;
		}

		return output;
	}

	
	public static void main(String[] args) throws IOException {
		new RequestHandler().sendData();
		new RequestHandler().sendPostData();
	}
	

}
