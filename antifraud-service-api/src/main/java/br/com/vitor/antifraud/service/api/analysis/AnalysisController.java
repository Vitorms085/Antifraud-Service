package br.com.vitor.antifraud.service.api.analysis;

import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.service.SuspiciousTransactionService;
import br.com.vitor.antifraud.service.model.suspicioustransaction.SuspiciousBankTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analises")
@RequiredArgsConstructor
public class AnalysisController {

    private final SuspiciousTransactionService suspiciousTransactionService;

    @GetMapping("/{id}")
    public AnalysisStatusDTO getAnalysisStatus(@PathVariable("id") UUID id) {
        return AnalysisStatusMapper.toDTO(suspiciousTransactionService.findByTransactionId(id));
    }

    @GetMapping("/suspeitas")
    public Page<AnalysisStatusDTO> findAllSuspicious(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return suspiciousTransactionService.findAll(pageable).map(AnalysisStatusMapper::toDTO);
    }
}
