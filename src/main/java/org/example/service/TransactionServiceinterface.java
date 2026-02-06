package org.example.service;

import org.springframework.transaction.annotation.Transactional;

public interface TransactionServiceinterface {
    @Transactional
    void transfer(int from, int to, int amount);

    @Transactional
    void credit(int id, int amount);

    @Transactional
    void debit(int id, int amount);

    @Transactional
    int getBalance(int id);
}
