package org.example.service;

import org.example.dtos.AccountDto;
import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("AccountService")
public class AccountService implements AccountServiceinterface {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("todos-service");



    private final AccountRepository accountRepository ;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository= accountRepository;
        logger.info("AccountRepository injected into TodoServiceImpl");
        logger.info("AccountRepository initialized");
    }

    @Override
    @Transactional
    public void createAccount(AccountDto account) {
        Account acc = new Account();
        acc.setName(account.getName());
        acc.setBalance(account.getBalance());

        accountRepository.save(acc);
    }

    @Transactional
    public Account getAccount(long id){
        return accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Account not found"));
    }





}
