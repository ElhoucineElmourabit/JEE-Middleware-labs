package com.example.backend.mappers;


import com.example.backend.dtos.*;
import com.example.backend.entities.AccountOperation;
import com.example.backend.entities.CurrentAccount;
import com.example.backend.entities.Customer;
import com.example.backend.entities.SavingAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer toCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingAccountDTO);
        savingAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDTO;
    }

    public SavingAccount toSavingAccount(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO, savingAccount);
        savingAccount.setCustomer(toCustomer(savingAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentAccountDTO);
        currentAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    public CurrentAccount toCurrentAccount(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO, currentAccount);
        currentAccount.setCustomer(toCustomer(currentAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountHistoryDTO fromAccountHistory(Page<AccountOperation> operations,
                                                String accountId,
                                                double balance,
                                                int page,
                                                int size) {
        AccountHistoryDTO historyDTO = new AccountHistoryDTO();
        historyDTO.setAccountOperationDTOs(
                operations.getContent()
                        .stream()
                        .map(this::fromAccountOperation)
                        .toList()
        );
        historyDTO.setAccountId(accountId);
        historyDTO.setBalance(balance);
        historyDTO.setCurrentPage(page);
        historyDTO.setPageSize(size);
        historyDTO.setTotalPages(operations.getTotalPages());
        return historyDTO;
    }
}