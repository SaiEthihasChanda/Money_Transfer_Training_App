package org.example;

import org.example.entity.Account;
import org.example.enums.AccountStatus;
import org.example.service.TransferService;
import org.example.service.TransferServiceImpl;

public class Application {
    public static void main(String[] args) {

        Account account_1 = new Account(1,"Ram",20000.00, AccountStatus.ACTIVE);
        Account account_2 = new Account(2,"Raja",10000.00,AccountStatus.ACTIVE);
        double amount = 3000.00;
        TransferService transferService = new TransferServiceImpl();
        transferService.transfer(account_1,account_2,amount);
        System.out.println("Account_1 Balance :" + account_1.getBalance());
        System.out.println("Account_2 balance :"+account_2.getBalance());
    }
}