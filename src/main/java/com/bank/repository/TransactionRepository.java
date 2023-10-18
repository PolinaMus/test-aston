package com.bank.repository;

import com.bank.model.Transaction;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
}
