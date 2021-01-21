package id.co.bfi.listenerchassis.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "list_token_access")
public class TokenAccessEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private UUID id;

	@Column(name = "apikey")
	private String apikey;

	@Column(name = "issued_at")
	private LocalDateTime issuedAt;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "scope")
	private String scope;

	@Column(name = "expires_in")
	private LocalDateTime expiresIn;

	public TokenAccessEntity() {
	}

	public TokenAccessEntity(UUID id, LocalDateTime issuedAt, String accessToken, String scope,
			LocalDateTime expiresIn) {
		this.id = id;
		this.issuedAt = issuedAt;
		this.accessToken = accessToken;
		this.scope = scope;
		this.expiresIn = expiresIn;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getApikey() {
		return apikey;
	}

	public LocalDateTime getExpiresIn() {
		return expiresIn;
	}

	public UUID getId() {
		return id;
	}

	public LocalDateTime getIssuedAt() {
		return issuedAt;
	}

	public String getScope() {
		return scope;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public void setExpiresIn(LocalDateTime expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setIssuedAt(LocalDateTime issuedAt) {
		this.issuedAt = issuedAt;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
