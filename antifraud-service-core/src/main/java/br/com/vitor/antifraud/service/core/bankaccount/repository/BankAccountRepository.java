package br.com.vitor.antifraud.service.core.bankaccount.repository;

import br.com.vitor.antifraud.service.model.bankaccount.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, UUID> {
}
