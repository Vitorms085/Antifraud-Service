package br.com.vitor.antifraud.service.core.transaction.exception;

import java.util.UUID;

public class UnderAnalysisException extends RuntimeException {

    public UnderAnalysisException(UUID transactionId, UUID bankAccountId) {
        super("Transaction with id " + transactionId + " suspicious because the bank account id "
                + bankAccountId + "is under analysis due to previous suspicious activity." );
    }
}
