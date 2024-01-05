package org.openframework.commons.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Hello world!
 *
 */
@Configuration
@EnableRedisRepositories
@ConditionalOnProperty(name = "org.openframework.commons.redis.enabled", havingValue = "true", matchIfMissing = false)
public class RedisConfiguration {

	private static boolean redisServerEnabled = false;

	@Value("${spring.redis.host:localhost}")
	private String redisHostname;

	@Value("${spring.redis.port:6379}")
	private Integer redisPort;

	public static boolean isRedisServerEnabled() {
		return redisServerEnabled;
	}

	public static void setRedisServerEnabled(boolean redisEnabled) {
		redisServerEnabled = redisEnabled;
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		System.out.println("redisHostname: " + redisHostname);
		System.out.println("redisPort: " + redisPort);
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHostname,
				redisPort);
		return new JedisConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new JdkSerializationRedisSerializer());
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();
		if(testConnection(template)) {
			setRedisServerEnabled(true);
		}
		return template;
	}

	private boolean testConnection(RedisTemplate<String, Object> template) {
		if (null != template && null != template.getConnectionFactory()) {
			if(null != template.getConnectionFactory()) {
				String pingResult = template.getConnectionFactory().getConnection().ping();
				System.out.println("INFO: redis server test connection is ok. ping result: " + pingResult);
				return "PONG".equalsIgnoreCase(pingResult);
			}
		}
		return false;
	}
}
