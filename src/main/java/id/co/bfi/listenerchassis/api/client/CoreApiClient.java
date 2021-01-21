package id.co.bfi.listenerchassis.api.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "coreApiClient", url = "${app.api.client.core.base-url}")
public interface CoreApiClient {

	@GetMapping(value = "/ping")
	ResponseEntity<String> ping(@RequestHeader Map<String, String> requestHeaders);

}
