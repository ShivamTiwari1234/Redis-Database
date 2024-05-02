package com.example.redisDemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
@Bean
   public LettuceConnectionFactory getConncetionFactory(){
    RedisStandaloneConfiguration redisStandaloneConfiguration =
             new RedisStandaloneConfiguration("redis-10793.c330.asia-south1-1.gce.cloud.redislabs.com",10793);
             redisStandaloneConfiguration.setPassword("pzhrWqzl9uLStSDMUiL4yzNJDDzNIx3L");

             LettuceConnectionFactory lettuceConnectionFactory=new LettuceConnectionFactory(redisStandaloneConfiguration);
             return lettuceConnectionFactory;
    }
    @Bean
    public RedisTemplate<String,Object> getTemplate(){
     RedisTemplate<String,Object> redisTemplate =new RedisTemplate<>();
     redisTemplate.setConnectionFactory(getConncetionFactory());
     redisTemplate.setKeySerializer(new StringRedisSerializer());
     redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

     return redisTemplate;
    }

}
