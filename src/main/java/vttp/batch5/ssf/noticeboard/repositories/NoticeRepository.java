package vttp.batch5.ssf.noticeboard.repositories;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import vttp.batch5.ssf.noticeboard.models.Notice;

@Repository
public class NoticeRepository {
	@Autowired
	@Qualifier("notice") // ensure same as bean in app config
	RedisTemplate<String, Object> template; //ensure same as app config

	// TODO: Task 4
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type
	//
	//
	// Write the redis-cli command that you use in this method in the comment.
	// For example if this method deletes a field from a hash, then write the
	// following
	// redis-cli command
	// hdel myhashmap a_key

	// redis command to get - GET notice:id
	public void insertNotice(String id, Notice notice) {
		String jsonString = Json.createObjectBuilder()
				.add("id", notice.getId())
				.add("title", notice.getTitle())
				.add("poster", notice.getPoster())
				.add("postDate", notice.getPostDateAsLong())
				.add("text", notice.getText())
				.build()
				.toString();

		template.opsForValue().set("notice:" + id, jsonString);
	}
	//to get random key
	public String getRandomKey() {
		Set<String> keys = template.keys("notice:*");
		if (keys != null) {
			int randomIndex = (int) (Math.random() * keys.size());
			return (String) keys.toArray()[randomIndex];
		}
		return null;
	}
}
