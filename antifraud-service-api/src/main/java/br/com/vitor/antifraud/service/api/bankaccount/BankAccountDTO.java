package br.com.vitor.antifraud.service.api.bankaccount;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BankAccountDTO {

    private UUID id;
    private BigDecimal creditLimit;
    private BigDecimal debitLimit;
    private Boolean isActive;
}
