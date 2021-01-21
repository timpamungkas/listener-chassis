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
import org.springframework.messaging.handler.annotation.Header;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import id.co.bfi.listenerchassis.api.client.CoreApiClient;
import id.co.bfi.listenerchassis.broker.message.SampleMessage;
import id.co.bfi.listenerchassis.exception.handler.DlxProcessingErrorHandler;

//@Service
public class SampleCoreListener {

	private static final Logger LOG = LoggerFactory.getLogger(SampleMessage.class);

	@Autowired
	private CoreApiClient coreApiClient;

	@Value("${app.api.client.core.apikey}")
	private String coreApikey;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("sampleErrorHandler")
	private DlxProcessingErrorHandler sampleErrorHandler;

	@RabbitListener(queues = "q.human-capital.master-employee-los.work", concurrency = "1-3")
	private void listenAndHitApi(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
			throws IOException {
		try {
			var sampleMessage = objectMapper.readValue(message.getBody(), SampleMessage.class);

			if (sampleMessage.getNumber() > 100) {
				throw new NumberFormatException("Wrong number");
			}

			LOG.debug("Receiving message : {}", message);
			var response = coreApiClient.ping(prepareRequestHeaders());
			LOG.debug("Response API : {}", response.getBody());
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			sampleErrorHandler.handleErrorProcessingMessage(message, channel, e);
		}
	}

	private HashMap<String, String> prepareRequestHeaders() {
		var requestHeaders = new HashMap<String, String>();
		requestHeaders.put("apikey", coreApikey);
		return requestHeaders;
	}
}
