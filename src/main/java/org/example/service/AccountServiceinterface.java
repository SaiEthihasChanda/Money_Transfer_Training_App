package org.example.service;

import org.example.dtos.AccountDto;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface AccountServiceinterface {
    @Transactional
    void createAccount(AccountDto account);

    @Transactional
    AccountDto getAccount(long id);

    @Transactional
    List<AccountDto> getAllAccounts();
}
