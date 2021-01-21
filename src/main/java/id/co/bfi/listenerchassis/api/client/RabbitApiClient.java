package id.co.bfi.listenerchassis.api.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "rabbitApiClient", url = "${app.api.client.rabbit.base-url}")
public interface RabbitApiClient {

	@GetMapping(value = "/api/echo")
	ResponseEntity<String> echo(@RequestHeader Map<String, String> requestHeaders,
			@RequestParam Map<String, String> queryParams, @RequestBody String requestBody);

}
