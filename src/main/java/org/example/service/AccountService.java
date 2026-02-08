package org.example.service;

import org.example.dtos.AccountDto;
import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        acc.setHolderName(account.getHolderName());
        acc.setBalance(account.getBalance());
        acc.setStatus(account.getStatus() != null ? account.getStatus() : "ACTIVE");
        acc.setLastupdatedAt(LocalDateTime.now());

        accountRepository.save(acc);
    }

    @Transactional
    public AccountDto getAccount(long id){
        Account account = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Account not found"));
        return mapToDto(account);
    }

    @Transactional
    public List<AccountDto> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(this::mapToDto).toList();
    }

    private AccountDto mapToDto(Account account) {
        return new AccountDto(
            account.getId(),
            account.getHolderName(),
            account.getStatus(),
            account.getLastupdatedAt(),
            account.getBalance()
        );
    }
}
