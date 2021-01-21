package id.co.bfi.listenerchassis.api.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notificationApiClient", url = "${app.api.client.notification.base-url}")
public interface NotificationApiClient {

	@PostMapping(value = "/api/v1/publish_notif/send_push_notif", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> sendNotif(@RequestHeader Map<String, String> requestHeaders,
			@RequestBody String requestBody);
}
