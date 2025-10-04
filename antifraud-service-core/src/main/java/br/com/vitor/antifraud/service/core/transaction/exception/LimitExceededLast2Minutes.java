package br.com.vitor.antifraud.service.core.transaction.exception;

import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;

import java.util.UUID;

public class LimitExceededLast2Minutes extends RuntimeException {

    public LimitExceededLast2Minutes(UUID bankAccountId, UUID transactionId, TransactionType type) {
        super("The " + type + " limit has been exceeded in the last 2 minutes for bank account " + bankAccountId + " on transaction " + transactionId);
    }
}
