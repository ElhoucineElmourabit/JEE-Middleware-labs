package com.example.backend.exceptions;

import lombok.Data;

@Data
public class BalanceNotSufficientException extends Exception {
    private final String msg;

    public BalanceNotSufficientException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
