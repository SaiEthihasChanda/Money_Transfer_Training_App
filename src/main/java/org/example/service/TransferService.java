package org.example.service;

import org.example.entity.Account;
import org.example.entity.TransactionLog;

public interface TransferService {
    TransactionLog transfer(Account fromAccount, Account toAccount, double amount);
}
