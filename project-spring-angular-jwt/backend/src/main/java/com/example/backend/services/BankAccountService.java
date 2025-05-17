package com.example.backend.services;

import com.example.backend.entities.BankAccount;
import com.example.backend.entities.CurrentAccount;
import com.example.backend.entities.Customer;
import com.example.backend.entities.SavingAccount;
import com.example.backend.exceptions.BalanceNotSufficientException;
import com.example.backend.exceptions.BankAccountNotFoundException;
import com.example.backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccount> bankAccountList();
}
