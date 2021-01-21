package id.co.bfi.listenerchassis.query.action;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import id.co.bfi.listenerchassis.api.client.TokenAccessApiClientV2;
import id.co.bfi.listenerchassis.api.response.TokenAccessResponse;
import id.co.bfi.listenerchassis.entity.TokenAccessEntity;
import id.co.bfi.listenerchassis.repository.TokenAccessRepository;
import id.co.bfi.listenerchassis.util.BasicAuthUtil;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

@Component
public class TokenAccessAction {

	@Autowired
	private TokenAccessRepository tokenAccessRepository;

	@Autowired
	private TokenAccessApiClientV2 tokenApiClient;

	public String getExistingTokenAccess(String username) {
		var existingTokenAccess = tokenAccessRepository.findByApikey(username);
		return existingTokenAccess.isEmpty() ? StringUtils.EMPTY : existingTokenAccess.get().getAccessToken();
	}

	public String getNewTokenAccess(String username, String password) {
		var tokenFromApigeeResponse = tokenApiClient.getToken(prepareTokenRequestHeaders(username, password));

		if (tokenFromApigeeResponse.getStatusCode().is2xxSuccessful()) {
			storeTokenAccess(tokenFromApigeeResponse.getBody());
			return tokenFromApigeeResponse.getBody().getAccessToken();
		} else {
			throw new IllegalArgumentException("Invalid credential for get access token");
		}
	}

	private void storeTokenAccess(TokenAccessResponse tokenAccessResponse) {
		var tokenAccessEntity = new TokenAccessEntity();
		tokenAccessEntity.setAccessToken(tokenAccessResponse.getAccessToken());

		var issuedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(tokenAccessResponse.getIssuedAt()), ZoneOffset.UTC);
		tokenAccessEntity.setIssuedAt(issuedAt);

		var expireTime = issuedAt.plusSeconds(tokenAccessResponse.getExpiresIn());
		tokenAccessEntity.setExpiresIn(expireTime);

		tokenAccessEntity.setScope(tokenAccessEntity.getScope());

		tokenAccessRepository.save(tokenAccessEntity);
	}

	private Map<String, String> prepareTokenRequestHeaders(String username, String password) {
		var headers = new HashMap<String, String>();

		headers.put(HttpHeaders.AUTHORIZATION, BasicAuthUtil.basicAuthString(username, password));
		headers.put(HttpHeaders.CONTENT_LENGTH, "0");

		return headers;
	}

	public void deleteExpiredTokens() {
		var nowInUtc = LocalDateTime.now(ZoneOffset.UTC);
		tokenAccessRepository.deleteExpiredTokens(nowInUtc);
	}
}
