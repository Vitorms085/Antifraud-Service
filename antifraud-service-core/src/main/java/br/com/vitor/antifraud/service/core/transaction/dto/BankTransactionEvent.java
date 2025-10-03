package br.com.vitor.antifraud.service.core.transaction.dto;

import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
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
}
