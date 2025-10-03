package br.com.vitor.antifraud.service.core.transaction.repository;

import br.com.vitor.antifraud.service.model.banktransaction.BankTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankTransactionRepository extends CrudRepository<BankTransaction, UUID> {
}
