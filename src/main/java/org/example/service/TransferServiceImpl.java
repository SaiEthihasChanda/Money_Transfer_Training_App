package org.example.service;

import org.example.entity.Account;
import org.example.entity.TransactionLog;
import org.example.enums.TransactionStatus;

import java.time.LocalDateTime;

public class TransferServiceImpl implements TransferService{

    private AccountService accountService=new AccountServiceImpl();
    public TransactionLog transfer(Account from, Account to, double amount){

        accountService.debit(from,amount);
        accountService.credit(to,amount);

        return new TransactionLog(
                1,
                from.getId(),
                to.getId(),
                amount,
                TransactionStatus.SUCCESS,
                LocalDateTime.now()
        );
    }

}
