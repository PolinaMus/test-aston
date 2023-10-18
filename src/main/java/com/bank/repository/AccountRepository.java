package com.bank.repository;

import com.bank.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);

    List<Account> findAll();

    Optional<Account> findById(Long accountId);

    Account updateBalance(Long id, BigDecimal newBalance);
}
