package org.example.service;

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
    public void transfer(int from, int to, int amount) {
        Account fromaccount = accountRepository.findById((long) from).orElseThrow(()-> new RuntimeException("Todo not found"));
        if(this.getBalance(from)>= amount) {
            this.credit(to, amount);
            this.debit(from, amount);
        }
        else{
            logger.error("Insufficient balance, transaction aborted");
        }

    }

    @Override
    @Transactional
    public void credit(int id, int amount){
        Account toaccount = accountRepository.findById((long) id).orElseThrow(()-> new RuntimeException("Todo not found"));;
        if (toaccount != null) {
            toaccount.setBalance(toaccount.getBalance()+amount);
            accountRepository.save(toaccount);
        }


    }

    @Override
    @Transactional
    public void debit(int id,int amount){
        Account fromaccount = accountRepository.findById((long) id).orElseThrow(()-> new RuntimeException("Todo not found"));;
        if (fromaccount != null) {
            fromaccount.setBalance(fromaccount.getBalance()-amount);
            accountRepository.save(fromaccount);
        }

    }
    @Override
    @Transactional
    public int getBalance(int id) {
        Account account = accountRepository.findById((long) id).orElseThrow(()-> new RuntimeException("Account not found"));;

            if (account != null) {
                return account.getBalance();
            }
            return -1;


    }
}
