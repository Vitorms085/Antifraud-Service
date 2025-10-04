package br.com.vitor.antifraud.service.core.bankaccount.exceptions;

import java.util.UUID;

public class BankAccountNotActiveException extends RuntimeException {

    public BankAccountNotActiveException(UUID id) {
        super("Bank account with id " + id + " is not active.");
    }
}
