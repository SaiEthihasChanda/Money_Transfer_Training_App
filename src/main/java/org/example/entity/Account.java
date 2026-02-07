package org.example.entity;

import org.example.enums.AccountStatus;

import java.time.LocalDateTime;

public class Account {
    private int id;
    private String holderName;
    private double balance;
    private AccountStatus status;
    private LocalDateTime lastUpdated;
    public Account(){}
    public Account(int id,String holderName,double balance,AccountStatus status){
        this.id=id;
        this.holderName=holderName;
        this.balance=balance;
        this.status=status;
        this.lastUpdated=LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }


    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastupdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}