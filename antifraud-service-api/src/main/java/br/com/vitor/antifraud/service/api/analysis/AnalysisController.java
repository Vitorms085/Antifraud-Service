package br.com.vitor.antifraud.service.api.analysis;

import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.dto.SuspiciousTransactionFilterDTO;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.service.SuspiciousTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/analises")
@RequiredArgsConstructor
public class AnalysisController {

    private final SuspiciousTransactionService suspiciousTransactionService;

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisStatusDTO> getAnalysisStatus(@PathVariable("id") UUID id) {
        AnalysisStatusDTO dto = AnalysisStatusMapper.toDTO(suspiciousTransactionService.findByTransactionId(id));

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/suspeitas")
    public ResponseEntity<Page<AnalysisStatusDTO>> findAllSuspicious(
            @Valid SuspiciousTransactionFilterDTO filter,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AnalysisStatusDTO> pagedDto = suspiciousTransactionService.findAll(filter, pageable)
                .map(AnalysisStatusMapper::toDTO);

        return ResponseEntity.ok(pagedDto);
    }

    @PatchMapping("/{transactionId}")
    public ResponseEntity<AnalysisStatusDTO> updateAnalysisStatus(
            @PathVariable UUID transactionId,
            @RequestBody @Valid UpdateAnalysisStatusRequest request) {
        return ResponseEntity.ok(
            AnalysisStatusMapper.toDTO(
                suspiciousTransactionService.updateStatus(transactionId, request.getStatus())
            )
        );
    }
}
