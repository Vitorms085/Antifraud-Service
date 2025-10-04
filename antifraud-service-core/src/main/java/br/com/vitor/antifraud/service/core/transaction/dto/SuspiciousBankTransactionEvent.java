package br.com.vitor.antifraud.service.core.transaction.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SuspiciousBankTransactionEvent {

    private UUID transactionId;
    private UUID bankAccountId;
    private String reason;
    private String status = "SUSPEITA_DE_FRAUDE";
    private LocalDateTime analysisDateTime = LocalDateTime.now();

    public SuspiciousBankTransactionEvent(BankTransactionEvent bankTransactionEvent, String reason) {
        this.transactionId = bankTransactionEvent.getTransactionId();
        this.bankAccountId = bankTransactionEvent.getBankAccountId();
        this.reason = reason;
    }
}
