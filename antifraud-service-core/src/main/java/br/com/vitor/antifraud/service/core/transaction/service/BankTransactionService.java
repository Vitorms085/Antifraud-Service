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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankTransactionService {

    private final BankTransactionRepository repository;
    private final BankTransactionValidatorService bankTransactionValidatorService;
    private final SuspiciousTransactionService suspiciousTransactionService;

    public void processTransaction(BankTransactionEvent event) {
        validateTransaction(event);

        BankTransaction bankTransaction = BankTransactionMapper.toEntity(event);

        repository.save(bankTransaction);
    }

    private void validateTransaction(BankTransactionEvent event) {
        try {
            bankTransactionValidatorService.validate(event);
        } catch (CreditLimitExceededException
                 | DebitLimitExceededException
                 | UnderAnalysisException
                 | LimitExceededLast2Minutes e) {
            suspiciousTransactionService.createAndPublish(event, e);
        }
    }

    public void saveTransactionAsFailed(BankTransactionEvent event, Exception e) {

    }
}
