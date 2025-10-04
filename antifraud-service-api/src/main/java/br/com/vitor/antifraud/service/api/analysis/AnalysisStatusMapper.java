package br.com.vitor.antifraud.service.api.analysis;

import br.com.vitor.antifraud.service.model.suspicioustransaction.SuspiciousBankTransaction;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AnalysisStatusMapper {

    public static AnalysisStatusDTO toDTO(SuspiciousBankTransaction entity) {
        AnalysisStatusDTO dto = new AnalysisStatusDTO();
        dto.setBankTransactionId(entity.getBankTransactionId());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
