package br.com.vitor.antifraud.service.core.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Log4j2
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private CompletableFuture<SendResult<String, Object>> send(String topic, String key, Object message) {
        return kafkaTemplate.send(topic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic={}, partition={}, offset={}, key={}, message={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset(),
                                key,
                                message);
                    } else {
                        log.error("Failed to send message to topic={}, key={}, message={}", topic, key, message, ex);
                    }
                });
    }

    public CompletableFuture<SendResult<String, Object>> send(String topic, Object message) {
        return send(topic, UUID.randomUUID().toString(), message);
    }
}
