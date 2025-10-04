package br.com.vitor.antifraud.service.api.analysis;

import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class AnalysisStatusDTO {

    private UUID bankTransactionId;
    private String reason;
    private AnalysisStatus status;
}
