package br.com.vitor.antifraud.service.api.bankaccount;

import br.com.vitor.antifraud.service.model.bankaccount.BankAccount;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BankAccountMapper {

    public static BankAccountDTO toDTO(BankAccount bankAccount) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(bankAccount.getId());
        dto.setCreditLimit(bankAccount.getCreditLimit());
        dto.setDebitLimit(bankAccount.getDebitLimit());
        dto.setIsActive(bankAccount.isActive());

        return dto;
    }
}
