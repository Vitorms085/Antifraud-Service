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
import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankTransactionValidatorService {
    private final BankAccountRepository bankAccountRepository;
    private final SuspiciousTransactionRepository suspiciousTransactionRepository;

    public void validate(BankTransactionEvent event) {
        BankAccount bankAccount = validateBankAccountExists(event);

        validateCreditDebitLimit(event, bankAccount);
        validatePendingAnalysis(event, bankAccount);
    }

    private BankAccount validateBankAccountExists(BankTransactionEvent event) {
        BankAccount bankAccount = bankAccountRepository.findById(event.getBankAccountId())
                .orElseThrow(() -> new BankAccountNotFoundException(event.getBankAccountId()));

        if (!bankAccount.isActive()) {
            throw new BankAccountNotActiveException(event.getBankAccountId());
        }

        return bankAccount;
    }

    private void validateCreditDebitLimit(BankTransactionEvent event, BankAccount bankAccount) {
        switch (event.getType()) {
            case DEBIT -> {
                if (bankAccount.getDebitLimit().compareTo(event.getValue()) < 0) {
                    throw new DebitLimitExceededException(event.getTransactionId());
                }
            }
            case CREDIT -> {
                if (bankAccount.getCreditLimit().compareTo(event.getValue()) < 0) {
                    throw new CreditLimitExceededException(event.getTransactionId());
                }
            }
            default -> throw new IllegalArgumentException("Unsupported transaction type: " + event.getType());
        }
    }

    private void validatePendingAnalysis(BankTransactionEvent event, BankAccount bankAccount) {
        final LocalDateTime currentDateTime = LocalDateTime.now();

        if (suspiciousTransactionRepository.existsPendingAnalysisInTheLast7Days(bankAccount.getId(),
                currentDateTime.minusDays(7L), AnalysisStatus.UNDER_ANALYSIS)) {
            throw new UnderAnalysisException(event.getTransactionId(), bankAccount.getId());
        }

        Optional<BigDecimal> sumOpt = suspiciousTransactionRepository.sumTransactionsValueInTheLast2Minutes(
                currentDateTime.minusMinutes(2L), event.getType());

        if (sumOpt.isPresent() && sumOpt.get().add(event.getValue()).compareTo(bankAccount.getLimitByType(event.getType())) > 0) {
            throw new LimitExceededLast2Minutes(bankAccount.getId(), event.getTransactionId(), event.getType());
        }
    }
}
