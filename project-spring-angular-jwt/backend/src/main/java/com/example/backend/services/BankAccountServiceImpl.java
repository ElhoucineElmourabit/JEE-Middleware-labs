package com.example.backend.services;

import com.example.backend.dtos.*;
import com.example.backend.entities.*;
import com.example.backend.entities.enums.OperationType;
import com.example.backend.exceptions.BalanceNotSufficientException;
import com.example.backend.exceptions.BankAccountNotFoundException;
import com.example.backend.exceptions.CustomerNotFoundException;
import com.example.backend.mappers.BankAccountMapperImpl;
import com.example.backend.repositories.AccountOperationRepository;
import com.example.backend.repositories.BankAccountRepository;
import com.example.backend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl mapper;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving a new customer");
        Customer customer = mapper.toCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException {
        log.info("Update a customer");
        Customer customer = mapper.toCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(String.format("Client introuvable : aucun client avec l'ID fourni : %d", customerId)
                        ));
        return mapper.fromCustomer(customer);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.findByNameContains(keyword)
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null){
            throw new CustomerNotFoundException(String.format("Client introuvable : aucun client avec l'ID fourni : %d", customerId));
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);
        return mapper.fromCurrentAccount(savedCurrentAccount);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null){
            throw new CustomerNotFoundException(String.format("Client introuvable : aucun client avec l'ID fourni : %d", customerId));
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);

        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);
        return mapper.fromSavingAccount(savedSavingAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->
                    new BankAccountNotFoundException(String.format("Compte bancaire introuvable : aucun compte bancaire avec l'ID fourni : %d", accountId))
                    );
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return mapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return mapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->
                        new BankAccountNotFoundException(String.format("Compte bancaire introuvable : aucun compte bancaire avec l'ID fourni : %d", accountId))
                );
        if(bankAccount.getBalance() < amount){
            throw new BalanceNotSufficientException("Transaction impossible : solde insuffisant.");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->
                        new BankAccountNotFoundException(String.format("Compte bancaire introuvable : aucun compte bancaire avec l'ID fourni : %d", accountId))
                );

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList(){
        return bankAccountRepository.findAll()
                .stream()
                .map(account -> {
                    if (account instanceof SavingAccount) {
                        return mapper.fromSavingAccount((SavingAccount) account);
                    } else {
                        return mapper.fromCurrentAccount((CurrentAccount) account);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        return accountOperationRepository.findByBankAccountId(accountId)
                .stream()
                .map(mapper::fromAccountOperation)
                .collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElse(null);
        if(bankAccount == null ){
            throw new BankAccountNotFoundException(String.format("Compte bancaire introuvable : aucun compte bancaire avec l'ID fourni : %d", id));
        }
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(id, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO = mapper.fromAccountHistory(accountOperations, id, bankAccount.getBalance(), page, size);
        return accountHistoryDTO;
    }


}
