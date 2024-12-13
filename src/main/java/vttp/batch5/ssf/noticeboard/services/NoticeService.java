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

	@Value("${noticeboard.url}") // set in app properties, to hide *** REMEMBER TO SET THIS VARIABLE IN RAILWAY
	private String publisherUrl;

	public ResponseEntity<String> postToNoticeServer(Notice notice) {
			notice.setPostDate(new Date(System.currentTimeMillis()));
			notice.setId(UUID.randomUUID().toString());
			// handle categories[]
			JsonArrayBuilder categories = Json.createArrayBuilder();
			for (String category : notice.getCategories()) {
				categories.add(category);
			}
			// to object conversion
			JsonObject obj = Json.createObjectBuilder()
					.add("title", notice.getTitle())
					.add("poster", notice.getPoster())
					.add("postDate", notice.getPostDateAsLong()) // use getPostDateAsLong() instead
					.add("categories", categories.build())
					.add("text", notice.getText())
					.build();

					// make req entity - similar to day 17 slides 6 to 8***
			RequestEntity<String> requestEntity = RequestEntity
					.post(publisherUrl + "/notice")
					.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
					.header("Accept", MediaType.APPLICATION_JSON_VALUE)
					.body(obj.toString());

			// rest api call - similar to day17 slide 5*** (this works, dont change)
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			// if not error -> save to redis
			// collate by all error status codes
			if (!response.getStatusCode().isError()) {

				// to json string
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject jsonObject = jsonReader.readObject();
				String id = jsonObject.getString("id");
				Long timestamp = jsonObject.getJsonNumber("timestamp").longValue(); //not needed but if remove have error?

				// save to redis 
				noticeRepo.insertNotice(id, notice);
			}

			return response;
	}

	public ResponseEntity<String> checkHealth() {
		try {
			String key = noticeRepo.getRandomKey();
			String status;
			if(key !=null){
				status = "healthy";
			} else {
				status = "unhealthy";
			}

			JsonObject response = Json.createObjectBuilder()
					.add("status", status)
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
