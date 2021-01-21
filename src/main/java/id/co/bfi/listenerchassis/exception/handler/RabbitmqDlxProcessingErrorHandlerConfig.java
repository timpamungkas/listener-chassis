package id.co.bfi.listenerchassis.exception.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitmqDlxProcessingErrorHandlerConfig {

	private static final int MAX_RETRY_COUNT = 3;

	@Bean
	@Scope("prototype")
	DlxProcessingErrorHandler sampleErrorHandler() {
		return new DlxProcessingErrorHandler("x.human-capital.dead", MAX_RETRY_COUNT);
	}

}
