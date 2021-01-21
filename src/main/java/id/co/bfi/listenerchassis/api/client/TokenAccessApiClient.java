package id.co.bfi.listenerchassis.api.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import id.co.bfi.listenerchassis.api.response.TokenAccessResponse;

@FeignClient(name = "tokenApiClient", url = "${app.api.client.token.base-url}")
public interface TokenAccessApiClient {

	@PostMapping("/")
	ResponseEntity<TokenAccessResponse> getToken(@RequestHeader Map<String, String> requestHeaders);
}
