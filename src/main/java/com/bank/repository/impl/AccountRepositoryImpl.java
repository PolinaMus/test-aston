package com.bank.repository.impl;

import com.bank.model.Account;
import com.bank.repository.AccountRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.aston.bank.rest.db_beans.tables.records.AccountRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ru.aston.bank.rest.db_beans.tables.Account.ACCOUNT;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final DSLContext dslContext;

    public AccountRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Account save(Account account) {
        final AccountRecord accountRecord = (AccountRecord) accountToRecord(account);
        return dslContext
                .insertInto(ACCOUNT)
                .set(accountRecord)
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Error inserting record"))
                .map(this::recordToAccount);
    }

    @Override
    public List<Account> findAll() {
        return dslContext
                .select(ACCOUNT.NAME, ACCOUNT.BALANCE)
                .from(ACCOUNT)
                .fetch(this::recordToAccount);
    }

    @Override
    public Optional<Account> findById(Long accountId) {
        return dslContext
                .select()
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(accountId))
                .fetchOptional()
                .map(this::recordToAccount);
    }

    @Override
    public Account updateBalance(Long id, BigDecimal newBalance) {
        return dslContext
                .update(ACCOUNT)
                .set(ACCOUNT.BALANCE, newBalance)
                .where(ACCOUNT.ID.eq(id))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Error updating record"))
                .map(this::recordToAccount);
    }

    protected Record accountToRecord(Account account) {
        final AccountRecord accountRecord = new AccountRecord();
        accountRecord.setName(account.getName());
        accountRecord.setPin(account.getPin());
        accountRecord.setBalance(BigDecimal.ZERO);
        return accountRecord;
    }

    protected Account recordToAccount(Record record) {
        final Account account = new Account();
        if (record.field(ACCOUNT.ID) != null) {
            account.setId(record.get(ACCOUNT.ID));
        }
        if (record.field(ACCOUNT.NAME) != null) {
            account.setName(record.get(ACCOUNT.NAME));
        }
        if (record.field(ACCOUNT.PIN) != null) {
            account.setPin(record.get(ACCOUNT.PIN));
        }
        if (record.field(ACCOUNT.BALANCE) != null) {
            account.setBalance(record.get(ACCOUNT.BALANCE));
        }
        return account;
    }
}
