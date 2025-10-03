package br.com.vitor.antifraud.service.core.transaction.service;

import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
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

    public void processTransaction(BankTransactionEvent event) {
        bankTransactionValidatorService.validate(event);

        BankTransaction bankTransaction = BankTransactionMapper.toEntity(event);

        repository.save(bankTransaction);
    }
}
