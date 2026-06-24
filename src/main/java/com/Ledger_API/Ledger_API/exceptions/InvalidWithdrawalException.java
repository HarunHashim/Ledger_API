package com.Ledger_API.Ledger_API.exceptions;

public class InvalidWithdrawalException extends RuntimeException{
    public InvalidWithdrawalException(String message) {
        super(message);
    }
}
