package br.com.vitor.antifraud.service.core.transaction.exception;

import java.util.UUID;

public class CreditLimitExceededException extends RuntimeException {

    public CreditLimitExceededException(UUID id) {
        super("Credit transaction amount exceeds the credit limit for bank account with id " + id);
    }
}
