package com.bank.repository.impl;

import com.bank.model.Transaction;
import com.bank.repository.TransactionRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.aston.bank.rest.db_beans.tables.records.TransactionRecord;

import static ru.aston.bank.rest.db_beans.tables.Transaction.TRANSACTION;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final DSLContext dslContext;

    public TransactionRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Transaction save(Transaction transaction) {
        final TransactionRecord transactionRecord = (TransactionRecord) transactionToRecord(transaction);
        return dslContext
                .insertInto(TRANSACTION)
                .set(transactionRecord)
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Error inserting record"))
                .map(this::recordToTransaction);
    }

    protected Record transactionToRecord(Transaction transaction) {
        final TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setSourceAccountId(transaction.getSourceAccountId());
        transactionRecord.setTargetAccountId(transaction.getTargetAccountId());
        transactionRecord.setAmount(transaction.getAmount());
        transactionRecord.setPin(transaction.getPin());
        return transactionRecord;
    }

    protected Transaction recordToTransaction(Record record) {
        final Transaction transaction = new Transaction();
        if (record.field(TRANSACTION.ID) != null) {
            transaction.setId(record.get(TRANSACTION.ID));
        }
        if (record.field(TRANSACTION.SOURCE_ACCOUNT_ID) != null) {
            transaction.setSourceAccountId(record.get(TRANSACTION.SOURCE_ACCOUNT_ID));
        }
        if (record.field(TRANSACTION.TARGET_ACCOUNT_ID) != null) {
            transaction.setTargetAccountId(record.get(TRANSACTION.TARGET_ACCOUNT_ID));
        }
        if (record.field(TRANSACTION.AMOUNT) != null) {
            transaction.setAmount(record.get(TRANSACTION.AMOUNT));
        }
        if (record.field(TRANSACTION.PIN) != null) {
            transaction.setPin(record.get(TRANSACTION.PIN));
        }
        return transaction;
    }
}
