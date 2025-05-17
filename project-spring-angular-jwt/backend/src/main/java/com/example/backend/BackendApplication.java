package com.example.backend;

import com.example.backend.entities.*;
import com.example.backend.entities.enums.AccountStatus;
import com.example.backend.entities.enums.OperationType;
import com.example.backend.repositories.AccountOperationRepository;
import com.example.backend.repositories.BankAccountRepository;
import com.example.backend.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
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
    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository) {
        return args -> {
            BankAccount bankAccount = bankAccountRepository.findById("098f3bf7-5bc6-45dd-8786-ee7dc593f9e0").orElse(null);
            if (bankAccount != null) {
                System.out.println("********************");
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getStatus());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());
                System.out.println(bankAccount.getClass().getSimpleName());
                if (bankAccount instanceof CurrentAccount) {
                    System.out.println("Over draft => " + ((CurrentAccount) bankAccount).getOverDraft());
                } else {
                    System.out.println("Interest rate => " + ((SavingAccount) bankAccount).getInterestRate());
                }

                bankAccount.getOperations()
                        .forEach(op -> {
                            System.out.println("-----------------------------");
                            System.out.println(op.getId());
                            System.out.println(op.getType());
                            System.out.println(op.getAmount());
                            System.out.println(op.getOperationDate());
                        });
            }
            }
            ;
        }
}
