package org.example.service;

import org.example.entity.Account;
import org.example.enums.AccountStatus;
import org.example.exception.AccountNotActiveException;
import org.example.exception.InsufficientBalanceException;

public class AccountServiceImpl implements AccountService{
    public void credit(Account account, double amount){
        if(account.getStatus()!= AccountStatus.ACTIVE){
            throw new AccountNotActiveException("Account not Active");
        }
        else{
            account.setBalance(account.getBalance()+amount);
        }
    }
    public void debit(Account account,double amount){
        if(account.getStatus()!=AccountStatus.ACTIVE ){
            throw new AccountNotActiveException("Account not active");
        }
        else if(account.getBalance()<amount){
            throw new InsufficientBalanceException("Not enough balance to transfer money");
        }
        account.setBalance(account.getBalance()-amount);


    }
}
