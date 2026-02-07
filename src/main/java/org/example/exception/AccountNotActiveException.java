package org.example.exception;

public class AccountNotActiveException extends RuntimeException{
    public AccountNotActiveException(String msg){
        super(msg);
    }
}
