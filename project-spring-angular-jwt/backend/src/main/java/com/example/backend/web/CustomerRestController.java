package com.example.backend.web;

import com.example.backend.dtos.AccountHistoryDTO;
import com.example.backend.dtos.AccountOperationDTO;
import com.example.backend.dtos.CustomerDTO;
import com.example.backend.entities.Customer;
import com.example.backend.exceptions.BankAccountNotFoundException;
import com.example.backend.exceptions.CustomerNotFoundException;
import com.example.backend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return bankAccountService.searchCustomers(keyword);
    }


    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(
            @PathVariable(name = "id") Long customerId,
            @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable() Long id){
        bankAccountService.deleteCustomer(id);
    }

    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO accountHistory(
            @PathVariable String id,
            @RequestParam(name = "page", defaultValue = "0")int page,
            @RequestParam(name = "size", defaultValue = "5") int size ) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(id, page, size);
    }
}
