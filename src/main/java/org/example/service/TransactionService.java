package org.example.service;

import org.example.dtos.TransferDto;
import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("TransactionService")
public class TransactionService implements TransactionServiceinterface {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("todos-service");

    private final AccountRepository accountRepository ;

    @Autowired
    public TransactionService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        logger.info("TransactionRepository injected into TransactionService");
        logger.info("TransactionService initialized");
    }

    @Override
    @Transactional
    public void transfer(TransferDto transferDto) {
        Account fromAccount = accountRepository.findById(transferDto.getFromAccountId()).orElseThrow(()-> new RuntimeException("Account not found"));
        Account toAccount = accountRepository.findById(transferDto.getToAccountId()).orElseThrow(()-> new RuntimeException("Account not found"));

        if(fromAccount.getBalance() >= transferDto.getAmount()){
            toAccount.credit(transferDto.getAmount());
            fromAccount.debit(transferDto.getAmount());
            accountRepository.save(toAccount);
            accountRepository.save(fromAccount);
            logger.info("Transfer successful from account " + fromAccount.getId() + " to account " + toAccount.getId());
        }
        else{
            logger.error("Transfer failed - insufficient balance");
            throw new RuntimeException("Insufficient balance for transfer");
        }

    }
}
