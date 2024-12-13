package vttp.batch5.ssf.noticeboard.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class NoticeRepository {

	@Autowired
	@Qualifier("notice")
	RedisTemplate<String, Object> template;

	/* 
	 * redis-cli command
	 * set key value
	 */
	public void insertNotices(String key, JsonObject jsonPayload) {
		template.opsForValue().set(key, jsonPayload.toString());
	}

	/* 
	 * redis-cli command
	 * randomkey
	 */
	public String getRandomKey() {
		return template.randomKey();
	}

}
