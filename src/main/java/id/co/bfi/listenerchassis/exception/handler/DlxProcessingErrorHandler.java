package id.co.bfi.listenerchassis.exception.handler;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.lang.NonNull;

import com.rabbitmq.client.Channel;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * Generic class to handle RabbitMQ proccessing error that might occur on
 * <code>try-catch</code>. This will not handle invalid message conversion
 * though (for example if you has Employee JSON structure to process, but got
 * Animal JSON structure instead from Rabbit MQ queue).
 * </p>
 * 
 * <p>
 * In short, this is just a class to avoid boilerplate codes for your handler.
 * Default implementation is re-throw message to dead letter exchange, using
 * <code>DlxProcessingErrorHandler</code> class. The basic usage of the
 * interface is :<br/>
 * 
 * <pre>
 * public void handleMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
 * 	var jsonObjectToBeProcessed = null;
 * 
 * 	try {
 * 		jsonObjectToBeProcessed = objectMapper.readValue(new String(message.getBody()),
 * 				JsonObjectToBeProcessed.class);
 * 
 * 		// do real processing here
 * 		// ...
 * 		//
 * 
 * 		channel.basicAck(tag, false);
 * 	} catch (Exception e) {
 * 		processingErrorHandler.handleErrorProcessingMessage(message, channel, tag);
 * 	}
 * }
 * </pre>
 * 
 * @author timpamungkas
 *
 */
public class DlxProcessingErrorHandler {

	private static final int MAX_ALLOWED_RETRY_COUNT = 100;

	private static final int MIN_ALLOWED_RETRY_COUNT = 0;

	/**
	 * Dead exchange name
	 */
	@NonNull
	private String deadExchangeName;

	@Setter
	@Getter
	private Logger LOG = LoggerFactory.getLogger(DlxProcessingErrorHandler.class);

	private int maxRetryCount = 3;

	/**
	 * Constructor. Will retry for n times (default is 3) and on the next retry will
	 * consider message as dead, put it on dead exchange with given
	 * <code>dlxExchangeName</code> and <code>routingKey</code>
	 * 
	 * @param deadExchangeName dead exchange name. Not a dlx for work queue, but
	 *                         exchange name for really dead message (wont processed
	 *                         anymore).
	 * @throws IllegalArgumentException if <code>dlxExchangeName</code> or
	 *                                  <code>dlxRoutingKey</code> is null or empty.
	 */
	public DlxProcessingErrorHandler(String deadExchangeName) {
		super();

		if (StringUtils.isBlank(deadExchangeName)) {
			throw new IllegalArgumentException("Must define dlx exchange name");
		}

		this.deadExchangeName = deadExchangeName;
	}

	/**
	 * Constructor. Will retry for <code>maxRetryCount</code> times and on the next
	 * retry will consider message as dead, put it on dead exchange with given
	 * <code>dlxExchangeName</code> and <code>routingKey</code>
	 * 
	 * @param deadExchangeName dead exchange name. Not a dlx for work queue, but
	 *                         exchange name for really dead message (wont processed
	 *                         anymore).
	 * @param maxRetryCount    number of retry before message considered as dead (0
	 *                         >= <code> maxRetryCount</code> >= 1000). If set less
	 *                         than 0, will always retry
	 * @throws IllegalArgumentException if <code>dlxExchangeName</code> or
	 *                                  <code>dlxRoutingKey</code> is null or empty.
	 */
	public DlxProcessingErrorHandler(String deadExchangeName, int maxRetryCount) {
		this(deadExchangeName);
		setMaxRetryCount(maxRetryCount);
	}

	public String getDeadExchangeName() {
		return deadExchangeName;
	}

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	/**
	 * Handle AMQP message consume error. This default implementation will put
	 * message to dead letter exchange for <code>maxRetryCount</code> times, thus
	 * two variables are required when creating this object:
	 * <code>dlxExchangeName</code> and <code>dlxRoutingKey</code>. <br/>
	 * <code>maxRetryCount</code> is 3 by default, but you can set it using
	 * <code>setMaxRetryCount(int)</code>
	 * 
	 * @param message AMQP message that caused error
	 * @param channel channel for AMQP message
	 * @param e       the exception thrown
	 * @return <code>true</code> if error handler works sucessfully,
	 *         <code>false</code> otherwise
	 */
	public boolean handleErrorProcessingMessage(Message message, Channel channel, Exception e) {
		var rabbitMqHeader = new RabbitmqHeader(message.getMessageProperties().getHeaders());

		try {
			if (rabbitMqHeader.getFailedRetryCount() >= maxRetryCount) {
				// publish to dead and ack
				LOG.warn("[DEAD] Error on retry {} for message {} due to {} ", rabbitMqHeader.getFailedRetryCount(),
						new String(message.getBody()), e.getMessage());

				channel.basicPublish(getDeadExchangeName(), message.getMessageProperties().getReceivedRoutingKey(),
						null, message.getBody());
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} else {
				LOG.warn("[REQUEUE] Error on retry {} for message {} due to {} ", rabbitMqHeader.getFailedRetryCount(),
						new String(message.getBody()), e.getMessage());

				channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
			}
			return true;
		} catch (IOException ex) {
			LOG.warn("[HANDLER-FAILED] Error on retry {} for message {} due to {} ",
					rabbitMqHeader.getFailedRetryCount(), new String(message.getBody()), ex.getMessage());
		}

		return false;
	}

	public void setMaxRetryCount(int maxRetryCount) {
		if (maxRetryCount < MIN_ALLOWED_RETRY_COUNT || maxRetryCount > MAX_ALLOWED_RETRY_COUNT) {
			throw new IllegalArgumentException(
					"retry count must between " + MIN_ALLOWED_RETRY_COUNT + "-" + MAX_ALLOWED_RETRY_COUNT);
		}

		this.maxRetryCount = maxRetryCount;
	}

}
