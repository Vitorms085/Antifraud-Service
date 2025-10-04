package br.com.vitor.antifraud.service.api.bankaccount;

import br.com.vitor.antifraud.service.core.bankaccount.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService service;

    @GetMapping("/{id}")
    public BankAccountDTO findById(@PathVariable("id") UUID id) {
        return BankAccountMapper.toDTO(service.findById(id));
    }
}
