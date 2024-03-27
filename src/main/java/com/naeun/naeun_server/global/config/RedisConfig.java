package com.naeun.naeun_server.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Getter
@EnableRedisRepositories
public class RedisConfig {
    @Value("${SPRING_DATA_REDIS_HOST}")
    private String host;
    @Value("${SPRING_DATA_REDIS_PORT}")
    private int port;
    @Value("${SPRING_DATA_REDIS_PASSWORD}")
    private String password;
    @Value("${spring.profiles.active:}")
    private String activeProfile;

    // Connect
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);

        if (!activeProfile.equals("local"))
            configuration.setPassword(password);

        return new LettuceConnectionFactory(configuration);
    }

    // Serialize bytes
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
