package vttp.batch5.ssf.noticeboard.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;

@Service
public class NoticeService {

	Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	// Task 3
	public void postToNoticeServer(JsonObject jsonPayload) {
		logger.info(jsonPayload.toString());
	}
}
