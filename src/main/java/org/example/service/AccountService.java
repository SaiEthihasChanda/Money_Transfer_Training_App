package org.example.service;

import org.example.entity.Account;

public interface AccountService {
    void credit(Account account, double amount);
     void debit(Account account, double amount);
}
