package com.bank.controller;

import com.bank.dto.AccountBalanceDto;
import com.bank.dto.AccountResponseDto;
import com.bank.dto.AccountDto;
import com.bank.dto.TransactionDto;
import com.bank.exception.InvalidAmountException;
import com.bank.exception.InvalidPinException;
import com.bank.mapper.AccountMapper;
import com.bank.model.Account;
import com.bank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@Valid @RequestBody AccountDto accountDTO) {
        accountService.createAccount(accountDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/balances")
    public List<AccountBalanceDto> getAllAccountBalances() {
        final List<Account> allAccountBalances = accountService.getAllAccountBalances();
        return allAccountBalances.stream()
                .map(AccountMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@Valid @RequestBody AccountResponseDto accountResponseDto)
            throws AccountNotFoundException {
        Account account = accountService.getAccountById(accountResponseDto.getId());

        if (!accountService.verifyPin(account, accountResponseDto.getPin())) {
            throw new InvalidPinException();
        }

        BigDecimal amount = accountResponseDto.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        accountService.deposit(account, amount, accountResponseDto.getPin());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody AccountResponseDto accountResponseDto)
            throws AccountNotFoundException {
        Account account = accountService.getAccountById(accountResponseDto.getId());

        if (!accountService.verifyPin(account, accountResponseDto.getPin())) {
            throw new InvalidPinException();
        }

        BigDecimal amount = accountResponseDto.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        accountService.withdraw(account, amount, accountResponseDto.getPin());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransactionDto transactionDto)
            throws AccountNotFoundException {
        Account sourceAccount = accountService.getAccountById(transactionDto.getSourceAccountId());
        Account targetAccount = accountService.getAccountById(transactionDto.getTargetAccountId());

        if (!accountService.verifyPin(sourceAccount, transactionDto.getPin())) {
            throw new InvalidPinException();
        }

        BigDecimal amount = transactionDto.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        accountService.transfer(sourceAccount, targetAccount, amount, transactionDto.getPin());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
