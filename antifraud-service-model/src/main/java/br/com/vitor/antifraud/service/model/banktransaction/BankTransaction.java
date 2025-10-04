package br.com.vitor.antifraud.service.model.banktransaction;

import br.com.vitor.antifraud.service.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "bank_transaction")
public class BankTransaction extends BaseEntity {

    private UUID bankAccountId;

    @Column(name = "transaction_value")
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime dateTime;
}
