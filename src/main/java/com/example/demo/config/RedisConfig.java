package com.example.demo.config;

import com.example.demo.model.Item;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.cluster.nodes}")
    private List<String> clusterNodes;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(clusterNodes);
        return new LettuceConnectionFactory(clusterConfiguration);
    }

    @Bean
    public RedisTemplate<String, Item> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Item> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Item> serializer = new Jackson2JsonRedisSerializer<>(Item.class);
        template.setValueSerializer(serializer);
        return template;
    }
}
