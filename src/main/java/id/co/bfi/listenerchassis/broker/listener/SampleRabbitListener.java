package id.co.bfi.listenerchassis.broker.listener;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import id.co.bfi.listenerchassis.api.client.RabbitApiClient;
import id.co.bfi.listenerchassis.broker.message.SampleMessage;
import id.co.bfi.listenerchassis.exception.handler.DlxProcessingErrorHandler;

//@Service
public class SampleRabbitListener {

	private static final Logger LOG = LoggerFactory.getLogger(SampleMessage.class);

	@Autowired
	private RabbitApiClient rabbitApiClient;

	@Value("${app.api.client.rabbit.apikey}")
	private String rabbitApikey;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("sampleErrorHandler")
	private DlxProcessingErrorHandler sampleErrorHandler;

	@RabbitListener(queues = "q.human-capital.master-employee-los.work", concurrency = "1-3")
	private void listenAndHitApi(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
		try {
			var sampleMessage = objectMapper.readValue(message.getBody(), SampleMessage.class);

//			if (sampleMessage.getNumber() > 100) {
//				throw new NumberFormatException("Wrong number");
//			}

			LOG.debug("Receiving message : {}", message);
			var response = rabbitApiClient.echo(prepareRequestHeaders(), prepareQueryParams(),
					prepareRequestBody(sampleMessage));
			LOG.debug("Response API : {}", response.getBody());
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			sampleErrorHandler.handleErrorProcessingMessage(message, channel, e);
		}
	}

	private String prepareRequestBody(SampleMessage message) throws JsonProcessingException {
		return objectMapper.writeValueAsString(message);
	}

	private HashMap<String, String> prepareQueryParams() {
		var queryParams = new HashMap<String, String>();
		queryParams.put("key-one", "value-one");
		queryParams.put("key-two", "value-two");
		return queryParams;
	}

	private HashMap<String, String> prepareRequestHeaders() {
		var requestHeaders = new HashMap<String, String>();
		requestHeaders.put("apikey", rabbitApikey);
		return requestHeaders;
	}

}
