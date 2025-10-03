package br.com.vitor.antifraud.service.core.transaction.mapper;

import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import br.com.vitor.antifraud.service.model.banktransaction.BankTransaction;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BankTransactionMapper {

    public static BankTransaction toEntity(BankTransactionEvent event) {
        BankTransaction bankTransaction = new BankTransaction();
        bankTransaction.setId(event.getTransactionId());
        bankTransaction.setBankAccountId(event.getBankAccountId());
        bankTransaction.setValue(event.getValue());
        bankTransaction.setType(event.getType());
        bankTransaction.setDateTime(event.getDateTime());

        return bankTransaction;
    }
}
