package br.com.vitor.antifraud.service.core.transaction.service;

import br.com.vitor.antifraud.service.core.bankaccount.exceptions.BankAccountNotFoundException;
import br.com.vitor.antifraud.service.core.bankaccount.repository.BankAccountRepository;
import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankTransactionValidatorService {
    private final BankAccountRepository bankAccountRepository;

    public void validate(BankTransactionEvent event) {
        if (!bankAccountRepository.existsById(event.getBankAccountId())) {
            throw new BankAccountNotFoundException(event.getBankAccountId());
        }
    }
}
