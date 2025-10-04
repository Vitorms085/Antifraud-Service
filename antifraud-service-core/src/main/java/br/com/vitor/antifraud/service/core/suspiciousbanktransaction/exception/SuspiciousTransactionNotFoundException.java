package br.com.vitor.antifraud.service.core.suspiciousbanktransaction.exception;

import java.util.UUID;

public class SuspiciousTransactionNotFoundException extends RuntimeException {

    public SuspiciousTransactionNotFoundException(UUID bankTransactionId) {
        super("Suspicious transaction not found for bank transaction ID: " + bankTransactionId);
    }
}
