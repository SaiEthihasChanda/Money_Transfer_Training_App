package org.example.web;

import org.example.dtos.TransferDto;
import org.example.service.AccountService;
import org.example.dtos.AccountDto;
import org.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class ServerController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        System.out.println(accountDto.getHolderName());
        accountService.createAccount(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }

    @GetMapping(value = "/accounts/{id}",produces = {"application/json"})
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            AccountDto account = accountService.getAccount(id);
            return ResponseEntity.ok(account);
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping(value = "/accounts")
    public ResponseEntity<?> getAllAccounts() {
        try {
            return ResponseEntity.ok(accountService.getAllAccounts());
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping(value="/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferDto transferDto){
        try {
            transactionService.transfer(transferDto);
            return ResponseEntity.ok("Transfer successful");
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
