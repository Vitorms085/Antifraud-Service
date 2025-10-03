package br.com.vitor.antifraud.service.core.bankaccount.exceptions;

import java.util.UUID;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(UUID id) {
        super("Bank account not found with id: " + id);
    }
}
