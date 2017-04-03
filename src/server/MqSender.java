package server;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * RabbitMQ Sender based on Spring AMQP
 */
public class MqSender {

	private RabbitTemplate rabbitTemplate;

	public MqSender() {
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
				"classpath:config/rmqConfig.xml");
			

		rabbitTemplate = (RabbitTemplate) applicationContext
				.getBean("messageSender");
	}

	public void send(String data) {
		rabbitTemplate.convertAndSend("NettyMqServerSenderExchange", "", data);
	}
}
