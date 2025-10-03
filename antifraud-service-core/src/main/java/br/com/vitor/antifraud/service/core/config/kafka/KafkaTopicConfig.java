package br.com.vitor.antifraud.service.core.config.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Log4j2
@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.transactions}")
    private String transactionsTopic;

    @Bean
    public NewTopic transactionsTopic() {
        return TopicBuilder.name(transactionsTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
