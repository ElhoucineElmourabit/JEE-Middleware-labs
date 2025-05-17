package com.example.backend.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BankAccountNotFoundException extends Exception{
    private final String msg;

    public BankAccountNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
