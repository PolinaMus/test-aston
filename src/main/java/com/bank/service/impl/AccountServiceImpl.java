package com.bank.service.impl;

import com.bank.dto.AccountDto;
import com.bank.exception.InsufficientFundsException;
import com.bank.mapper.AccountMapper;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createAccount(AccountDto accountDTO) {
        Account account = AccountMapper.mapDtoToAccount(accountDTO);
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccountBalances() {
        return accountRepository.findAll();
    }

    @Override
    public boolean verifyPin(Account account, String providedPin) {
        String storedPin = account.getPin();
        return storedPin.equals(providedPin);
    }

    @Override
    public Account getAccountById(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deposit(Account account, BigDecimal amount, String pin) {
        final BigDecimal newBalance = account.getBalance().add(amount);

        accountRepository.updateBalance(account.getId(), newBalance);

        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(Math.toIntExact(account.getId()));
        transaction.setTargetAccountId(Math.toIntExact(account.getId()));
        transaction.setAmount(amount);
        transaction.setPin(pin);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void withdraw(Account account, BigDecimal amount, String pin) {
        final BigDecimal currentBalance = account.getBalance();

        if (currentBalance.compareTo(amount) >= 0) {
            BigDecimal newBalance = currentBalance.subtract(amount);

            accountRepository.updateBalance(account.getId(), newBalance);

            Transaction transaction = new Transaction();
            transaction.setSourceAccountId(Math.toIntExact(account.getId()));
            transaction.setTargetAccountId(Math.toIntExact(account.getId()));
            transaction.setAmount(amount);
            transaction.setPin(pin);
            transactionRepository.save(transaction);
        }
        else {
            throw new InsufficientFundsException();
        }
    }

    @Override
    @Transactional
    public void transfer(Account sourceAccount, Account targetAccount, BigDecimal amount, String pin) {
        final BigDecimal currentBalance = sourceAccount.getBalance();

        if (currentBalance.compareTo(amount) >= 0) {
            final BigDecimal sourceBalance = currentBalance.subtract(amount);
            final BigDecimal targetBalance = targetAccount.getBalance().add(amount);

            accountRepository.updateBalance(sourceAccount.getId(), sourceBalance);
            accountRepository.updateBalance(targetAccount.getId(), targetBalance);

            Transaction transaction = new Transaction();
            transaction.setSourceAccountId(Math.toIntExact(sourceAccount.getId()));
            transaction.setTargetAccountId(Math.toIntExact(targetAccount.getId()));
            transaction.setAmount(amount);
            transaction.setPin(pin);
            transactionRepository.save(transaction);
        }
        else {
            throw new InsufficientFundsException();
        }
    }
}
