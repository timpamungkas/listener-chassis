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

import id.co.bfi.listenerchassis.api.client.NotificationApiClient;
import id.co.bfi.listenerchassis.broker.message.NotificationMessage;
import id.co.bfi.listenerchassis.exception.handler.DlxProcessingErrorHandler;

//@Service
public class NotificationListener {

	private static final Logger LOG = LoggerFactory.getLogger(NotificationListener.class);

	@Autowired
	private NotificationApiClient notificationApiClient;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${app.api.client.notification.apikey}")
	private String apikey;

	@Autowired
	@Qualifier("sampleErrorHandler")
	private DlxProcessingErrorHandler sampleErrorHandler;

	@RabbitListener(queues = "q.test", concurrency = "1-2")
	private void listenAndHitApi(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
			throws IOException {
		try {
			var notificationMessage = objectMapper.readValue(message.getBody(), NotificationMessage.class);
			LOG.debug("Receiving message : {}", message);
			var response = notificationApiClient.sendNotif(prepareRequestHeaders(),
					objectMapper.writeValueAsString(notificationMessage));
			LOG.debug("Response API : {}", response.getBody());
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			sampleErrorHandler.handleErrorProcessingMessage(message, channel, e);
		}
	}

	private HashMap<String, String> prepareRequestHeaders() {
		var requestHeaders = new HashMap<String, String>();

		requestHeaders.put("apikey", apikey);
		requestHeaders.put("xxx", "12345");

		return requestHeaders;
	}
}
