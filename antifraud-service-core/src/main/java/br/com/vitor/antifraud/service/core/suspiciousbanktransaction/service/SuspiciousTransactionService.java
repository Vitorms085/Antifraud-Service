package br.com.vitor.antifraud.service.core.suspiciousbanktransaction.service;

import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.exception.SuspiciousTransactionNotFoundException;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.mapper.SuspiciousTransactionMapper;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.repository.SuspiciousTransactionRepository;
import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.dto.SuspiciousBankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.producer.SuspiciousTransactionProducer;
import br.com.vitor.antifraud.service.model.suspicioustransaction.SuspiciousBankTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuspiciousTransactionService {

    private final SuspiciousTransactionRepository repository;
    private final SuspiciousTransactionProducer suspiciousTransactionProducer;

    public void createAndPublish(BankTransactionEvent event, Exception e) {
        create(event, e);
        publish(event, e);
    }

    private void create(BankTransactionEvent event, Exception e) {
        SuspiciousBankTransaction suspiciousBankTransaction = SuspiciousTransactionMapper.toEntity(event, e);

        repository.save(suspiciousBankTransaction);
    }

    private void publish(BankTransactionEvent event, Exception e) {
        suspiciousTransactionProducer.publish(new SuspiciousBankTransactionEvent(event, e.getMessage()));
    }

    public SuspiciousBankTransaction findByTransactionId(UUID bankTransactionId) {
        return repository.findByBankTransactionId(bankTransactionId)
                .orElseThrow(() -> new SuspiciousTransactionNotFoundException(bankTransactionId));
    }

    public Page<SuspiciousBankTransaction> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
