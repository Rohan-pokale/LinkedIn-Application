package com.linkedIn.post_service.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic PostCreatedTopic(){
        return new NewTopic("post-created-topic",3,(short) 1);
    }

    @Bean
    public NewTopic PostLikeTopic(){
        return new NewTopic("post-like-topic",3,(short) 1);
    }
}
