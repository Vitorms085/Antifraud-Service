package br.com.vitor.antifraud.service.core.suspiciousbanktransaction.service;

import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.dto.SuspiciousTransactionFilterDTO;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.exception.SuspiciousTransactionNotFoundException;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.mapper.SuspiciousTransactionMapper;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.repository.SuspiciousTransactionRepository;
import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.dto.SuspiciousBankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.producer.SuspiciousTransactionProducer;
import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import br.com.vitor.antifraud.service.model.suspicioustransaction.SuspiciousBankTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuspiciousTransactionServiceTest {

    @Mock
    private SuspiciousTransactionRepository repository;

    @Mock
    private SuspiciousTransactionProducer suspiciousTransactionProducer;

    @InjectMocks
    private SuspiciousTransactionService service;

    @Test
    void shouldCreateAndPublishSuspiciousTransaction() {
        var event = createSampleBankTransactionEvent();
        var exception = new RuntimeException("Test exception");
        var suspiciousTransaction = new SuspiciousBankTransaction();

        try (MockedStatic<SuspiciousTransactionMapper> mapper = Mockito.mockStatic(SuspiciousTransactionMapper.class)) {
            mapper.when(() -> SuspiciousTransactionMapper.toEntity(event, exception))
                    .thenReturn(suspiciousTransaction);

            service.createAndPublish(event, exception);

            verify(repository).save(suspiciousTransaction);
            verify(suspiciousTransactionProducer).publish(any(SuspiciousBankTransactionEvent.class));
        }
    }

    @Test
    void shouldFindByTransactionId() {
        var transactionId = UUID.randomUUID();
        var suspiciousTransaction = new SuspiciousBankTransaction();
        when(repository.findByBankTransactionId(transactionId)).thenReturn(Optional.of(suspiciousTransaction));

        var result = service.findByTransactionId(transactionId);

        assertNotNull(result);
        assertEquals(suspiciousTransaction, result);
    }

    @Test
    void shouldThrowWhenTransactionNotFound() {
        var transactionId = UUID.randomUUID();
        when(repository.findByBankTransactionId(transactionId)).thenReturn(Optional.empty());

        assertThrows(SuspiciousTransactionNotFoundException.class,
                () -> service.findByTransactionId(transactionId));
    }

    @Test
    void shouldFindAllWithFilters() {
        var filter = createSampleFilter();
        var pageable = Pageable.unpaged();
        var suspiciousTransaction = new SuspiciousBankTransaction();
        var page = new PageImpl<>(List.of(suspiciousTransaction));

        when(repository.findAllWithFilters(
                filter.getStartDate(),
                filter.getEndDate(),
                filter.getBankAccountId(),
                pageable
        )).thenReturn(page);

        Page<SuspiciousBankTransaction> result = service.findAll(filter, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldUpdateStatus() {
        var transactionId = UUID.randomUUID();
        var suspiciousTransaction = new SuspiciousBankTransaction();
        suspiciousTransaction.setStatus(AnalysisStatus.UNDER_ANALYSIS);

        when(repository.findByBankTransactionId(transactionId)).thenReturn(Optional.of(suspiciousTransaction));
        when(repository.save(any(SuspiciousBankTransaction.class))).thenAnswer(i -> i.getArgument(0));

        var result = service.updateStatus(transactionId, AnalysisStatus.APPROVED);

        assertNotNull(result);
        assertEquals(AnalysisStatus.APPROVED, result.getStatus());
        assertNotNull(result.getUpdatedAt());
    }

    private BankTransactionEvent createSampleBankTransactionEvent() {
        return BankTransactionEvent.builder()
                .transactionId(UUID.randomUUID())
                .bankAccountId(UUID.randomUUID())
                .value(BigDecimal.valueOf(100))
                .type(TransactionType.CREDIT)
                .dateTime(LocalDateTime.now())
                .build();
    }

    private SuspiciousTransactionFilterDTO createSampleFilter() {
        var filter = new SuspiciousTransactionFilterDTO();
        filter.setStartDate(LocalDateTime.now().minusDays(7));
        filter.setEndDate(LocalDateTime.now());
        filter.setBankAccountId(UUID.randomUUID());
        return filter;
    }
}
