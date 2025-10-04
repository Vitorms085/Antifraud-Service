package br.com.vitor.antifraud.service.api.analysis;

import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAnalysisStatusRequest {
    @NotNull(message = "Status is required")
    private AnalysisStatus status;
}
