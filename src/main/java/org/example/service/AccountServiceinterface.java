package org.example.service;

import org.example.dtos.AccountDto;
import org.example.entity.Account;
import org.springframework.transaction.annotation.Transactional;

public interface AccountServiceinterface {
    @Transactional
    void createAccount(AccountDto account);
}
