package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	@Value("${publishing.server.url}")
	String SERVER_URL;

	@Autowired
	NoticeRepository noticeRepository;

	Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	// Task 3
	// Submits a POST request and if successful, returns the notice id from the server.
	// If unsuccessful, returns an error message.
	public String postToNoticeServer(JsonObject jsonPayload) {
		
		RestTemplate template = new RestTemplate();
		
		// Building request
		RequestEntity<String> request = RequestEntity
		.post(URI.create(SERVER_URL + "notice"))
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(jsonPayload.toString());
		
		// Submit POST request to the server endpoint
		try {
			logger.info("Sending POST >>> " + jsonPayload);
			ResponseEntity<String> response = template.exchange(request, String.class);
			
			String responseBody = response.getBody();
			logger.info("Received " + response.getStatusCode() + " >>> " + responseBody);

			// Store response payload into a JsonObject
			JsonReader jsonReader = Json.createReader(new StringReader(responseBody));
			JsonObject jsonObject = jsonReader.readObject();
			
			// Add successful response to Redis
			try {
				String redisIdKey = jsonObject.getString("id");
				logger.info(jsonObject.toString());
				noticeRepository.insertNotices(redisIdKey, jsonObject);
				logger.info("Successfully added notice id " + redisIdKey + " to Redis");
				return redisIdKey;
			} catch (Exception e) {
				logger.error(e.toString());
				return e.toString();
			}
			
		// Catch and log HTTP exceptions
		} catch (HttpStatusCodeException e) {
			logger.error("Status code: " + e.getStatusCode() + ", " + e.getResponseBodyAsString());
			JsonReader jsonReader = Json.createReader(new StringReader(e.getResponseBodyAsString()));
			JsonObject jsonObject = jsonReader.readObject();
			return (jsonObject.getString("message"));
		}
	}

	// Task 6
	public Boolean isRedisHealthy() {
		String randKey = noticeRepository.getRandomKey();
        return randKey != null;
	}
}
