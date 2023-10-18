package com.bank.mapper;

import com.bank.dto.AccountBalanceDto;
import com.bank.dto.AccountDto;
import com.bank.model.Account;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccountMapper {

    public static Account mapDtoToAccount(AccountDto accountDTO) {
        final Account account = new Account();
        account.setName(accountDTO.getName());
        account.setPin(accountDTO.getPin());
        account.setBalance(accountDTO.getBalance());
        return account;
    }

    public static AccountBalanceDto mapToDto(Account account) {
        final AccountBalanceDto accountBalanceDto = new AccountBalanceDto();
        accountBalanceDto.setName(account.getName());
        accountBalanceDto.setBalance(account.getBalance());
        return accountBalanceDto;
    }
}
