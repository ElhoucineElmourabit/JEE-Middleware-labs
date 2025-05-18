package com.example.backend;

import com.example.backend.dtos.BankAccountDTO;
import com.example.backend.dtos.CurrentAccountDTO;
import com.example.backend.dtos.CustomerDTO;
import com.example.backend.dtos.SavingAccountDTO;
import com.example.backend.entities.*;
import com.example.backend.entities.enums.AccountStatus;
import com.example.backend.entities.enums.OperationType;
import com.example.backend.exceptions.BalanceNotSufficientException;
import com.example.backend.exceptions.BankAccountNotFoundException;
import com.example.backend.exceptions.CustomerNotFoundException;
import com.example.backend.repositories.AccountOperationRepository;
import com.example.backend.repositories.BankAccountRepository;
import com.example.backend.repositories.CustomerRepository;
import com.example.backend.services.BankAccountService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){

        return args -> {
            Stream.of("Hassan", "Yassine", "Piastri")
                    .forEach( name->{
                        Customer customer = new Customer();
                        customer.setName(name);
                        customer.setEmail(name+"@gmail.com");
                        customerRepository.save(customer);
                    });

            customerRepository.findAll()
                    .forEach( customer -> {
                        CurrentAccount currentAccount = new CurrentAccount();
                        currentAccount.setId(UUID.randomUUID().toString());
                        currentAccount.setBalance(Math.random()*90000);
                        currentAccount.setCreatedAt(new Date());
                        currentAccount.setStatus(AccountStatus.CREATED);
                        currentAccount.setCustomer(customer);
                        currentAccount.setOverDraft(9000);
                        bankAccountRepository.save(currentAccount);

                        SavingAccount savingAccount = new SavingAccount();
                        savingAccount.setId(UUID.randomUUID().toString());
                        savingAccount.setBalance(Math.random()*90000);
                        savingAccount.setCreatedAt(new Date());
                        savingAccount.setStatus(AccountStatus.CREATED);
                        savingAccount.setCustomer(customer);
                        savingAccount.setInterestRate(5.5);
                        bankAccountRepository.save(savingAccount);
                    });

            bankAccountRepository.findAll()
                    .forEach( bankAccount -> {
                        for (int i = 0; i < 10; i++){
                            AccountOperation accountOperation = new AccountOperation();
                            accountOperation.setOperationDate(new Date());
                            accountOperation.setAmount(Math.random()*12000);
                            accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                            accountOperation.setBankAccount(bankAccount);
                            accountOperationRepository.save(accountOperation);
                        }
                    });


        };
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Hassan", "Yassine", "Piastri")
                    .forEach( name->{
                        CustomerDTO customerDTO = new CustomerDTO();
                        customerDTO.setName(name);
                        customerDTO.setEmail(name+"@gmail.com");
                        bankAccountService.saveCustomer(customerDTO);
                    });
            bankAccountService.listCustomers()
                    .forEach( customer -> {
                        try {
                            bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
                            bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());

                        } catch (CustomerNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for(BankAccountDTO bankAccount: bankAccounts){
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingAccountDTO){
                        accountId = ((SavingAccountDTO) bankAccount).getId();
                    } else {
                        accountId = ((CurrentAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId, 1000 + Math.random()*12000, "CREDIT");
                    bankAccountService.debit(accountId, 1000 + Math.random()*9000,"DEBIT" );
                }
            }
        };
    }
}
