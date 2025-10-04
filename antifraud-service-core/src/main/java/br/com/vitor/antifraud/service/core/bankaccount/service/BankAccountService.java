package br.com.vitor.antifraud.service.core.bankaccount.service;

import br.com.vitor.antifraud.service.core.bankaccount.exceptions.BankAccountNotFoundException;
import br.com.vitor.antifraud.service.core.bankaccount.repository.BankAccountRepository;
import br.com.vitor.antifraud.service.model.bankaccount.BankAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository repository;


    public BankAccount findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new BankAccountNotFoundException(id));
    }
}
