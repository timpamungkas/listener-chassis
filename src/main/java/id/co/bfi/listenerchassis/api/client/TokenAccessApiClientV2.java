package id.co.bfi.listenerchassis.api.client;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import id.co.bfi.listenerchassis.api.response.TokenAccessResponse;

@Service
public class TokenAccessApiClientV2 {

	private RestTemplate restTemplate;

	@Value("${app.api.client.token.base-url}")
	private String tokenBaseUrl;

	@PostConstruct
	private void postConstruct() {
		this.restTemplate = new RestTemplate();
	}

	public ResponseEntity<TokenAccessResponse> getToken(@RequestHeader Map<String, String> requestHeaders) {
		var headers = new HttpHeaders();
		requestHeaders.forEach((k, v) -> headers.add(k, v));
		var requestEntity = new HttpEntity<>(null, headers);

		return restTemplate.exchange(tokenBaseUrl, HttpMethod.POST, requestEntity, TokenAccessResponse.class);
	}
}
