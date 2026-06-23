package com.Ledger_API.Ledger_API.exceptions;

public class InvalidTransferException extends RuntimeException {
    public InvalidTransferException (String message){
        super (message);
    }
}
