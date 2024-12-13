package vttp.batch5.ssf.noticeboard.services;

import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {
	@Autowired
	NoticeRepository repo;
	// TODO: Task 3
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type
	RestTemplate rt = new RestTemplate();
	private static final String apiURL = "https://publishing-production-d35a.up.railway.app/";

	public ResponseEntity<String> postToNoticeServer(Notice notice) {
		notice.setId(UUID.randomUUID().toString());
		notice.setPostDate(System.currentTimeMillis());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept", "application/json");

		HttpEntity<Notice> request = new HttpEntity<>(notice, headers);

		ResponseEntity<String> resp = rt.postForEntity(apiURL, request, String.class);

		if (resp.getStatusCode().is2xxSuccessful()) {
			repo.insertNotices(notice.getId(), notice);
		}
		return resp;
	}
}
