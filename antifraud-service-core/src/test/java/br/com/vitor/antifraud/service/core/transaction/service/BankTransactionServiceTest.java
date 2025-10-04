package br.com.vitor.antifraud.service.core.transaction.service;

import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.service.SuspiciousTransactionService;
import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.exception.CreditLimitExceededException;
import br.com.vitor.antifraud.service.core.transaction.exception.DebitLimitExceededException;
import br.com.vitor.antifraud.service.core.transaction.exception.LimitExceededLast2Minutes;
import br.com.vitor.antifraud.service.core.transaction.exception.UnderAnalysisException;
import br.com.vitor.antifraud.service.core.transaction.mapper.BankTransactionMapper;
import br.com.vitor.antifraud.service.core.transaction.repository.BankTransactionRepository;
import br.com.vitor.antifraud.service.model.banktransaction.BankTransaction;
import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankTransactionServiceTest {

    @Mock
    private BankTransactionRepository repository;

    @Mock
    private BankTransactionValidatorService bankTransactionValidatorService;

    @Mock
    private SuspiciousTransactionService suspiciousTransactionService;

    @InjectMocks
    private BankTransactionService bankTransactionService;

    @Test
    void shouldProcessTransactionSuccessfully() {
        var event = createSampleBankTransactionEvent();
        var bankTransaction = new BankTransaction();

        try (MockedStatic<BankTransactionMapper> mapper = Mockito.mockStatic(BankTransactionMapper.class)) {
            mapper.when(() -> BankTransactionMapper.toEntity(event)).thenReturn(bankTransaction);

            bankTransactionService.processTransaction(event);

            verify(bankTransactionValidatorService).validate(event);
            verify(repository).save(bankTransaction);
            verify(suspiciousTransactionService, never()).createAndPublish(any(), any());
        }
    }

    @Test
    void shouldCreateSuspiciousTransactionWhenCreditLimitExceeded() {
        var event = createSampleBankTransactionEvent();
        doThrow(new CreditLimitExceededException(event.getTransactionId()))
                .when(bankTransactionValidatorService).validate(event);

        bankTransactionService.processTransaction(event);

        verify(suspiciousTransactionService).createAndPublish(eq(event), any(CreditLimitExceededException.class));
    }

    @Test
    void shouldCreateSuspiciousTransactionWhenDebitLimitExceeded() {
        var event = createSampleBankTransactionEvent();
        doThrow(new DebitLimitExceededException(event.getTransactionId()))
                .when(bankTransactionValidatorService).validate(event);

        bankTransactionService.processTransaction(event);

        verify(suspiciousTransactionService).createAndPublish(eq(event), any(DebitLimitExceededException.class));
    }

    @Test
    void shouldCreateSuspiciousTransactionWhenUnderAnalysis() {
        var event = createSampleBankTransactionEvent();
        doThrow(new UnderAnalysisException(event.getTransactionId(), event.getBankAccountId()))
                .when(bankTransactionValidatorService).validate(event);

        bankTransactionService.processTransaction(event);

        verify(suspiciousTransactionService).createAndPublish(eq(event), any(UnderAnalysisException.class));
    }

    @Test
    void shouldCreateSuspiciousTransactionWhenLimitExceededLast2Minutes() {
        var event = createSampleBankTransactionEvent();
        doThrow(new LimitExceededLast2Minutes(event.getBankAccountId(), event.getTransactionId(), event.getType()))
                .when(bankTransactionValidatorService).validate(event);

        bankTransactionService.processTransaction(event);

        verify(suspiciousTransactionService).createAndPublish(eq(event), any(LimitExceededLast2Minutes.class));
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
}
