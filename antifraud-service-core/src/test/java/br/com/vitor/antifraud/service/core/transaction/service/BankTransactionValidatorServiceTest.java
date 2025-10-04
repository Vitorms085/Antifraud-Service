package br.com.vitor.antifraud.service.core.transaction.service;

import br.com.vitor.antifraud.service.core.bankaccount.exceptions.BankAccountNotActiveException;
import br.com.vitor.antifraud.service.core.bankaccount.exceptions.BankAccountNotFoundException;
import br.com.vitor.antifraud.service.core.bankaccount.repository.BankAccountRepository;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.repository.SuspiciousTransactionRepository;
import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import br.com.vitor.antifraud.service.core.transaction.exception.CreditLimitExceededException;
import br.com.vitor.antifraud.service.core.transaction.exception.DebitLimitExceededException;
import br.com.vitor.antifraud.service.core.transaction.exception.LimitExceededLast2Minutes;
import br.com.vitor.antifraud.service.core.transaction.exception.UnderAnalysisException;
import br.com.vitor.antifraud.service.model.bankaccount.BankAccount;
import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankTransactionValidatorServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private SuspiciousTransactionRepository suspiciousTransactionRepository;

    @InjectMocks
    private BankTransactionValidatorService validatorService;

    @Test
    void shouldValidateSuccessfully() {
        var event = createSampleBankTransactionEvent(TransactionType.CREDIT);
        var bankAccount = createSampleBankAccount();

        when(bankAccountRepository.findById(event.getBankAccountId())).thenReturn(Optional.of(bankAccount));
        when(suspiciousTransactionRepository.existsPendingAnalysisInTheLast7Days(
                eq(bankAccount.getId()), any(), eq(AnalysisStatus.UNDER_ANALYSIS)
        )).thenReturn(false);
        when(suspiciousTransactionRepository.sumTransactionsValueInTheLast2Minutes(any(), eq(event.getType())))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validatorService.validate(event));
    }

    @Test
    void shouldThrowWhenBankAccountNotFound() {
        var event = createSampleBankTransactionEvent(TransactionType.CREDIT);
        when(bankAccountRepository.findById(event.getBankAccountId())).thenReturn(Optional.empty());

        assertThrows(BankAccountNotFoundException.class, () -> validatorService.validate(event));
    }

    @Test
    void shouldThrowWhenBankAccountNotActive() {
        var event = createSampleBankTransactionEvent(TransactionType.CREDIT);
        var bankAccount = createSampleBankAccount();
        bankAccount.setIsActive(false);

        when(bankAccountRepository.findById(event.getBankAccountId())).thenReturn(Optional.of(bankAccount));

        assertThrows(BankAccountNotActiveException.class, () -> validatorService.validate(event));
    }

    @Test
    void shouldThrowWhenCreditLimitExceeded() {
        var event = createSampleBankTransactionEvent(TransactionType.CREDIT);
        event.setValue(BigDecimal.valueOf(2000));
        var bankAccount = createSampleBankAccount();

        when(bankAccountRepository.findById(event.getBankAccountId())).thenReturn(Optional.of(bankAccount));

        assertThrows(CreditLimitExceededException.class, () -> validatorService.validate(event));
    }

    @Test
    void shouldThrowWhenDebitLimitExceeded() {
        var event = createSampleBankTransactionEvent(TransactionType.DEBIT);
        event.setValue(BigDecimal.valueOf(2000));
        var bankAccount = createSampleBankAccount();

        when(bankAccountRepository.findById(event.getBankAccountId())).thenReturn(Optional.of(bankAccount));

        assertThrows(DebitLimitExceededException.class, () -> validatorService.validate(event));
    }

    @Test
    void shouldThrowWhenAccountUnderAnalysis() {
        var event = createSampleBankTransactionEvent(TransactionType.CREDIT);
        var bankAccount = createSampleBankAccount();

        when(bankAccountRepository.findById(event.getBankAccountId())).thenReturn(Optional.of(bankAccount));
        when(suspiciousTransactionRepository.existsPendingAnalysisInTheLast7Days(
                eq(bankAccount.getId()), any(), eq(AnalysisStatus.UNDER_ANALYSIS)
        )).thenReturn(true);

        assertThrows(UnderAnalysisException.class, () -> validatorService.validate(event));
    }

    @Test
    void shouldThrowWhenLimitExceededInLast2Minutes() {
        var event = createSampleBankTransactionEvent(TransactionType.CREDIT);
        var bankAccount = createSampleBankAccount();

        when(bankAccountRepository.findById(event.getBankAccountId())).thenReturn(Optional.of(bankAccount));
        when(suspiciousTransactionRepository.existsPendingAnalysisInTheLast7Days(
                eq(bankAccount.getId()), any(), eq(AnalysisStatus.UNDER_ANALYSIS)
        )).thenReturn(false);
        when(suspiciousTransactionRepository.sumTransactionsValueInTheLast2Minutes(any(), eq(event.getType())))
                .thenReturn(Optional.of(bankAccount.getCreditLimit()));

        assertThrows(LimitExceededLast2Minutes.class, () -> validatorService.validate(event));
    }

    private BankTransactionEvent createSampleBankTransactionEvent(TransactionType type) {
        return BankTransactionEvent.builder()
                .transactionId(UUID.randomUUID())
                .bankAccountId(UUID.randomUUID())
                .value(BigDecimal.valueOf(100))
                .type(type)
                .dateTime(LocalDateTime.now())
                .build();
    }

    private BankAccount createSampleBankAccount() {
        var account = new BankAccount();
        account.setId(UUID.randomUUID());
        account.setIsActive(true);
        account.setCreditLimit(BigDecimal.valueOf(1000));
        account.setDebitLimit(BigDecimal.valueOf(1000));
        return account;
    }
}
