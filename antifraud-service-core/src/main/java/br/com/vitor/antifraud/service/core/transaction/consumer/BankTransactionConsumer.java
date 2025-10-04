package br.com.vitor.antifraud.service.core.transaction.consumer;

import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.service.BankTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class BankTransactionConsumer {

    private final BankTransactionService bankTransactionService;

    @KafkaListener(topics = "${kafka.topic.transactions}", containerFactory = "bankTransactionContainerFactory")
    public void consume(BankTransactionEvent event) {
        log.info("Received bank transaction event: {}", event);

        try {
            bankTransactionService.processTransaction(event);
        } catch (Exception e) {
            log.error("Error processing bank transaction event: {}", event, e);
        }

        log.info("Finished processing bank transaction event: {}", event);
    }
}
