package br.com.vitor.antifraud.service.core.transaction.exception;

import java.util.UUID;

public class DebitLimitExceededException extends RuntimeException {

    public DebitLimitExceededException(UUID id) {
        super("Debit transaction amount exceeds the debit limit for bank account with id " + id);
    }
}
