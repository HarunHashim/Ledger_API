package com.Ledger_API.Ledger_API.exceptions;

public class InvalidDepositException extends RuntimeException{
    public InvalidDepositException(String message) {
        super(message);
    }
}
