package br.com.vitor.antifraud.service.core.suspiciousbanktransaction.mapper;

import br.com.vitor.antifraud.service.core.transaction.dto.BankTransactionEvent;
import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import br.com.vitor.antifraud.service.model.suspicioustransaction.SuspiciousBankTransaction;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SuspiciousTransactionMapper {

    public static SuspiciousBankTransaction toEntity(BankTransactionEvent event, Exception e) {
        SuspiciousBankTransaction entity = new SuspiciousBankTransaction();
        entity.setBankTransactionId(event.getTransactionId());
        entity.setReason(e.getMessage());
        entity.setStatus(AnalysisStatus.UNDER_ANALYSIS);

        return entity;
    }
}
