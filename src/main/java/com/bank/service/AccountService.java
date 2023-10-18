package com.bank.service;

import com.bank.dto.AccountDto;
import com.bank.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    void createAccount(AccountDto accountDTO);

    List<Account> getAllAccountBalances();

    boolean verifyPin(Account account, String providedPin);

    Account getAccountById(Long id) throws AccountNotFoundException;

    void deposit(Account account, BigDecimal amount, String pin);

    void withdraw(Account account, BigDecimal amount, String pin);

    void transfer(Account sourceAccount, Account targetAccount, BigDecimal amount, String pin);
}
