package com.bank.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Invalid amount");
    }
}
