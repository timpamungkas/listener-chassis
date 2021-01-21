package id.co.bfi.listenerchassis.api.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "sendTokenApiClient", url = "${app.api.client.bficonfins.base-url}")
public interface BfiConfinsApiClient {

	@GetMapping("/bficonfins/general/v1/get_data_branch")
	ResponseEntity<String> getDataBranch(@RequestHeader Map<String, String> requestHeaders);
}
