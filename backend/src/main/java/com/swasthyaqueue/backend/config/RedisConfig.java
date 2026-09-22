package com.swasthyaqueue.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//this class defines beans(reusable objects) that spring should create and manage at startup,
//rather than at business logic.
@Configuration//it tell how things work rather than what thing does 
public class RedisConfig {
    
    //marks a method whose return value spring should manage as a reusable object("Bean"), available for @Autowired injection 
    //anywhere else in the app
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }


}
