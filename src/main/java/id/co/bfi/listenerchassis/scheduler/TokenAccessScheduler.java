package id.co.bfi.listenerchassis.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import id.co.bfi.listenerchassis.query.service.TokenAccessService;

@Component
public class TokenAccessScheduler {

	@Autowired
	private TokenAccessService tokenAccessService;

	@Scheduled(fixedDelay = 1000)
	private void deleteExpiredTokens() {
		tokenAccessService.deleteExpiredTokens();
	}

}
