package de.pretrendr.boot.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

/**
 * Redis configuration. Still buggy.
 * 
 * @author Tristan Schneider
 */
@Configuration
public class RedisConfig {
	@Value("${redis.host}")
	private String host;

	@Value("${redis.port}")
	private int port;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {

		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName(host);
		jedisConFactory.setPort(port);
		return jedisConFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return template;
	}

	// @Bean
	// MessageListenerAdapter messageListener() {
	// return new MessageListenerAdapter(new RedisMessageSubscriber());
	// }
	//
	// @Bean
	// RedisMessageListenerContainer redisContainer() {
	// final RedisMessageListenerContainer container = new
	// RedisMessageListenerContainer();
	// container.setConnectionFactory(jedisConnectionFactory());
	// container.addMessageListener(messageListener(), topic());
	// return container;
	// }
	//
	// @Bean
	// MessagePublisher redisPublisher() {
	// return new RedisMessagePublisher(redisTemplate(), topic());
	// }
	//
	// @Bean
	// ChannelTopic topic() {
	// return new ChannelTopic("pubsub:queue");
	// }
}