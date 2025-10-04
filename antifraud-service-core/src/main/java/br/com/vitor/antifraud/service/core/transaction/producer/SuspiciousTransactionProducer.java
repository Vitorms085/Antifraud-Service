package br.com.vitor.antifraud.service.core.transaction.producer;

import br.com.vitor.antifraud.service.core.kafka.KafkaProducer;
import br.com.vitor.antifraud.service.core.transaction.dto.SuspiciousBankTransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class SuspiciousTransactionProducer {

    @Value("${kafka.topic.suspicious}")
    private String topic;
    private final KafkaProducer kafkaProducer;

    public boolean publish(SuspiciousBankTransactionEvent event) {
        try {
            kafkaProducer.send(topic, event).get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
