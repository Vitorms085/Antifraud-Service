package br.com.vitor.antifraud.service.model.bankaccount;

import br.com.vitor.antifraud.service.model.BaseEntity;
import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "bank_account")
public class BankAccount extends BaseEntity {

    private BigDecimal creditLimit;
    private BigDecimal debitLimit;
    private Boolean isActive;

    public boolean isActive() {
        return isActive != null && isActive;
    }

    public BigDecimal getLimitByType(TransactionType type) {
        return switch (type) {
            case CREDIT -> creditLimit;
            case DEBIT -> debitLimit;
        };
    }
}
