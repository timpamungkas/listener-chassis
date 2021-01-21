package id.co.bfi.listenerchassis.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import id.co.bfi.listenerchassis.entity.TokenAccessEntity;

public interface TokenAccessRepository extends CrudRepository<TokenAccessEntity, String> {

	Optional<TokenAccessEntity> findByApikey(String apikey);

	@Query(nativeQuery = true, value = "delete from list_token_access where expires_in < :nowInUtc")
	void deleteExpiredTokens(LocalDateTime nowInUtc);

}
