package org.example.config;

import org.example.model.PhoneRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfigure {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }


    @Bean
    public RedisTemplate<String, PhoneRecord> redisTemplate() {
        RedisTemplate<String, PhoneRecord> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(RedisSerializer.string());
        Jackson2JsonRedisSerializer<PhoneRecord> redisSerializer = new Jackson2JsonRedisSerializer<>(PhoneRecord.class);
        template.setValueSerializer(redisSerializer);
        return template;
    }
}
