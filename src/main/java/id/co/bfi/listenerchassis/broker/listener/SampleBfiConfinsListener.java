package id.co.bfi.listenerchassis.broker.listener;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import id.co.bfi.listenerchassis.api.client.BfiConfinsApiClient;
import id.co.bfi.listenerchassis.broker.message.SampleMessage;
import id.co.bfi.listenerchassis.exception.handler.DlxProcessingErrorHandler;
import id.co.bfi.listenerchassis.query.service.TokenAccessService;

@Service
public class SampleBfiConfinsListener {

	private static final Logger LOG = LoggerFactory.getLogger(SampleMessage.class);

	@Autowired
	private BfiConfinsApiClient bfiConfinsApiClient;

	@Value("${app.api.client.bficonfins.apikey}")
	private String bfiConfinsApikey;

	@Value("${app.api.client.bficonfins.apiSecret}")
	private String bfiConfinsApiSecret;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("sampleErrorHandler")
	private DlxProcessingErrorHandler sampleErrorHandler;

	@Autowired
	private TokenAccessService tokenAccessService;

	@RabbitListener(queues = "q.test", concurrency = "1-3")
	private void listenAndHitPublicApi(Message message, Channel channel,
			@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
		try {
			var sampleMessage = objectMapper.readValue(message.getBody(), SampleMessage.class);

			if (sampleMessage.getNumber() > 100) {
				throw new NumberFormatException("Wrong number");
			}

			// check bearer token
			var bearerToken = tokenAccessService.getValidTokenAccess(bfiConfinsApikey, bfiConfinsApiSecret);

			LOG.debug("Receiving message : {}", message);
			LOG.debug("Bearer token : {}", bearerToken);
			var response = bfiConfinsApiClient.getDataBranch(prepareRequestHeaders(bearerToken));
			LOG.debug("Response API : {}", response.getBody());
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			sampleErrorHandler.handleErrorProcessingMessage(message, channel, e);
		}
	}

	private HashMap<String, String> prepareRequestHeaders(String bearerToken) {
		var requestHeaders = new HashMap<String, String>();
		requestHeaders.put(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
		return requestHeaders;
	}
}
