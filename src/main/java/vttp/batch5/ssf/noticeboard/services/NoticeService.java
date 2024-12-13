package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {
	// TODO: Task 3
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type
	@Autowired
	private NoticeRepository noticeRepo;

	@Value("${noticeboard.url}")
	private String publisherUrl;

	//private static final String publisherUrl = "https://publishing-production-d35a.up.railway.app";

	public ResponseEntity<String> postToNoticeServer(Notice notice) {
			notice.setPostDate(new Date(System.currentTimeMillis()));
			notice.setId(UUID.randomUUID().toString());
			// handle categories[]
			JsonArrayBuilder categories = Json.createArrayBuilder();
			for (String category : notice.getCategories()) {
				categories.add(category);
			}
			// to object
			JsonObject obj = Json.createObjectBuilder()
					.add("title", notice.getTitle())
					.add("poster", notice.getPoster())
					.add("postDate", notice.getPostDateAsLong()) // Use getPostDateAsLong() instead
					.add("categories", categories.build())
					.add("text", notice.getText())
					.build();

			RequestEntity<String> requestEntity = RequestEntity
					.post(publisherUrl + "/notice")
					.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
					.header("Accept", MediaType.APPLICATION_JSON_VALUE)
					.body(obj.toString());

			// rest api call
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			// if not error -> save to redis
			// collate by all error status codes
			if (!response.getStatusCode().isError()) {

				// to json string
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject jsonObject = jsonReader.readObject();
				String id = jsonObject.getString("id");
				Long timestamp = jsonObject.getJsonNumber("timestamp").longValue();

				// save to redis 
				noticeRepo.insertNotice(id, notice);
			}

			return response;

		
	}

	public ResponseEntity<String> checkHealth() {
		try {
			String key = noticeRepo.getRandomKey();
			JsonObject response = Json.createObjectBuilder()
					.add("status", key != null ? "healthy" : "unhealthy")
					.add("timestamp", System.currentTimeMillis())
					.build();

			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(response.toString());
		} catch (Exception e) {
			JsonObject response = Json.createObjectBuilder()
					.add("status", "unhealthy")
					.add("timestamp", System.currentTimeMillis())
					.build();

			return ResponseEntity.status(503)
					.contentType(MediaType.APPLICATION_JSON)
					.body(response.toString());
		}
	}

}
