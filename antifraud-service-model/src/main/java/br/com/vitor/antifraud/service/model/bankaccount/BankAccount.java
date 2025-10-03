package br.com.vitor.antifraud.service.model.bankaccount;

import br.com.vitor.antifraud.service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "bank_account")
public class BankAccount extends BaseEntity {

    private String accountNumber;
    private String accountHolderName;
    private String bankName;
}
