package br.com.vitor.antifraud.service.core.transaction.dto;

import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BankTransactionEvent {

    private UUID transactionId;
    private UUID bankAccountId;
    private BigDecimal value;
    private TransactionType type;
    private LocalDateTime dateTime;

    public BankTransactionEvent() {
    }

    @Builder
    public BankTransactionEvent(UUID transactionId, UUID bankAccountId, BigDecimal value, TransactionType type, LocalDateTime dateTime) {
        this.transactionId = transactionId;
        this.bankAccountId = bankAccountId;
        this.value = value;
        this.type = type;
        this.dateTime = dateTime;
    }
}
