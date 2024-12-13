package vttp.batch5.ssf.noticeboard.services;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.JsonObject;

@Service
public class NoticeService {

	@Value("publishing.server.url")
	String SERVER_URL;

	// TODO: fix URL


	Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	// Task 3
	public JsonObject postToNoticeServer(JsonObject jsonPayload) {
		logger.info("Sending POST >>> " + jsonPayload.toString());

		RestTemplate template = new RestTemplate();

		RequestEntity<JsonObject> request = RequestEntity
			.post(URI.create("https://publishing-production-d35a.up.railway.app/notice"))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(jsonPayload);


			
		try {
			ResponseEntity<JsonObject> response = template.exchange(request, JsonObject.class);
			JsonObject responseBody = response.getBody();
			logger.info(responseBody.toString());
			return responseBody;
			
		} catch (HttpStatusCodeException e) {
			logger.error("Status code: " + e.getStatusCode() + ", " + e.getResponseBodyAsString());
			return e.getResponseBodyAs(JsonObject.class);
		}




	}


}
