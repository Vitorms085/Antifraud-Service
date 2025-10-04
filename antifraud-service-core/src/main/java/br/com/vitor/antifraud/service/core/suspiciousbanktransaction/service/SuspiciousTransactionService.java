package br.com.vitor.antifraud.service.core.suspiciousbanktransaction.service;

import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.dto.SuspiciousTransactionFilterDTO;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.exception.SuspiciousTransactionNotFoundException;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.mapper.SuspiciousTransactionMapper;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.repository.SuspiciousTransactionRepository;
import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.dto.SuspiciousBankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.producer.SuspiciousTransactionProducer;
import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import br.com.vitor.antifraud.service.model.suspicioustransaction.SuspiciousBankTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    void create(BankTransactionEvent event, Exception e) {
        SuspiciousBankTransaction suspiciousBankTransaction = SuspiciousTransactionMapper.toEntity(event, e);

        repository.save(suspiciousBankTransaction);
    }

    void publish(BankTransactionEvent event, Exception e) {
        suspiciousTransactionProducer.publish(new SuspiciousBankTransactionEvent(event, e.getMessage()));
    }

    public SuspiciousBankTransaction findByTransactionId(UUID bankTransactionId) {
        return repository.findByBankTransactionId(bankTransactionId)
                .orElseThrow(() -> new SuspiciousTransactionNotFoundException(bankTransactionId));
    }

    public Page<SuspiciousBankTransaction> findAll(SuspiciousTransactionFilterDTO filter, Pageable pageable) {
        return repository.findAllWithFilters(
                filter.getStartDate(),
                filter.getEndDate(),
                filter.getBankAccountId(),
                pageable
        );
    }

    @Transactional
    public SuspiciousBankTransaction updateStatus(UUID transactionId, AnalysisStatus newStatus) {
        SuspiciousBankTransaction transaction = findByTransactionId(transactionId);
        transaction.setStatus(newStatus);
        transaction.setUpdatedAt(LocalDateTime.now());

        return repository.save(transaction);
    }
}
