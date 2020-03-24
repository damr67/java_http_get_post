package com.cristi.shopify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ApiInterface {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Hello World");
		
		String RequestURL = "https://imequify.myshopify.com/admin/api/2020-01/products.json";
		
		// Auth values
		String user, password;
		user = "46f63e7b4e17c2666dd5f8452bcc3261";
		password = "1e5b6801523dc37995bda9877e4973d6";	
		
		getTemplate(RequestURL, user, password);
		postTemplate(RequestURL, user, password);
	}
	
	static void getTemplate(String RequestURL, String user, String password) {
		// Input Readers
		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();		
		
		try {
			// Request URL
			URL url = new URL(RequestURL);

			// Setup HttpUrl Connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			// Request Setup
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			// Setup Authentication
			String auth = user + ":" + password;
			
			// Encode Auth Data
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));

			
			String authHeaderValue = "Basic " + new String(encodedAuth);
			connection.setRequestProperty("Authorization", authHeaderValue);
			
			// Get Response input stream
			int status = connection.getResponseCode();
			
			
			// On Connection Error
			if(status > 299) {
			  reader = new BufferedReader(new  InputStreamReader(connection.getErrorStream()));
			  
			  while((line = reader.readLine()) != null) {
				  responseContent.append(line);
			  }
			  
			  // Close Stream Reader
			  reader.close();
			} else {
		      // Successful Connection
			  reader = new BufferedReader(new  InputStreamReader(connection.getInputStream()));
				  
			  while((line = reader.readLine()) != null) {
				responseContent.append(line);
			  }
			  
			  //Close Stream Reader
			  reader.close();
			}
			
			// Close Connection
			connection.disconnect();
			
			// Ouput API response
			System.out.println(responseContent.toString());
			
		} catch (MalformedURLException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	static void postTemplate(String RequestURL, String user, String password) {
		// Input Readers
		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();		
		
		try {
			// Request URL
			URL url = new URL(RequestURL);

			// Setup HttpUrl Connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			// Request Setup
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			
			// Setup Authentication
			String auth = user + ":" + password;
			
			// Encode Auth Data
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));

			
			String authHeaderValue = "Basic " + new String(encodedAuth);
			connection.setRequestProperty("Authorization", authHeaderValue);
			
			//Send Post Request
			// TODO: Find a better of represent Body json Data
			String jsonInputString = "{\r\n" + 
					"  \"product\": {\r\n" + 
					"    \"title\": \"producto test damr67 From Java\",\r\n" + 
					"    \"body_html\": \"<strong>Hola!</strong>\",\r\n" + 
					"    \"vendor\": \"Blum\",\r\n" + 
					"    \"product_type\": \"Vendor\"\r\n" + 
					"  }\r\n" + 
					"}";
			
			try(OutputStream os = connection.getOutputStream()) {
			    byte[] input = jsonInputString.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}
			
			try(BufferedReader br = new BufferedReader(
			  new InputStreamReader(connection.getInputStream(), "utf-8"))) {
			    StringBuilder response = new StringBuilder();
			    String responseLine = null;
			    while ((responseLine = br.readLine()) != null) {
			        response.append(responseLine.trim());
			    }
			    System.out.println(response.toString());
			}

			// Close Connection
			connection.disconnect();
			
		} catch (MalformedURLException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}

}
