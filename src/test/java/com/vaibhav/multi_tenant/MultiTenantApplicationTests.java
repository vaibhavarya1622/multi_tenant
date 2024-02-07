package com.vaibhav.multi_tenant;

import com.vaibhav.multi_tenant.model.tenant.User;
import com.vaibhav.multi_tenant.repository.tenant.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MultiTenantApplicationTests {
	@Value("${spring.kafka.producer.topic.dbOperator}")
	private String topic;
	@Autowired
	private KafkaTemplate<String,Object> kafkaTemplate;

	@Autowired
	private UserRepository userRepository;
	@Test
	void contextLoads() {
		List<User> users = new ArrayList<>();
		for(long i = 1L; i<=5; ++i){
			User user = User.builder()
							.id(i)
							.firstName("random first name")
							.lastName("random last name")
							.address("abc society")
							.phone("123")
							.email("abc@gmail.com")
							.build();
			users.add(user);
		}
		for(User user:users){
			Message<User> message = MessageBuilder.withPayload(user)
												  .setHeader(KafkaHeaders.TOPIC,topic)
												  .setHeader("tenant","usa")
												  .build();
			kafkaTemplate.send(message);
		}
		for(User user:users){
			Message<User> message = MessageBuilder.withPayload(user)
												  .setHeader(KafkaHeaders.TOPIC,topic)
												  .setHeader("tenant","india")
												  .build();
			kafkaTemplate.send(message);
		}
	}
}
