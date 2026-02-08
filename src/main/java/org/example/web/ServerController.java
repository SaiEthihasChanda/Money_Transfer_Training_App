package org.example.web;

import org.example.dtos.TransferDto;
import org.example.service.AccountService;
import org.example.dtos.AccountDto;
import org.example.service.TransactionLogService;
import org.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ServerController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionLogService transactionLogService;

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

    @GetMapping(value = "/transactions/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(transactionLogService.getTransactionById(id));
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping(value = "/transactions/account/{accountId}")
    public ResponseEntity<?> getAccountTransactionHistory(@PathVariable Long accountId) {
        try {
            return ResponseEntity.ok(transactionLogService.getAccountTransactionHistory(accountId));
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping(value = "/transactions/from/{fromAccountId}")
    public ResponseEntity<?> getTransactionsByFromAccount(@PathVariable Long fromAccountId) {
        try {
            return ResponseEntity.ok(transactionLogService.getTransactionsByFromAccount(fromAccountId));
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping(value = "/transactions/to/{toAccountId}")
    public ResponseEntity<?> getTransactionsByToAccount(@PathVariable Long toAccountId) {
        try {
            return ResponseEntity.ok(transactionLogService.getTransactionsByToAccount(toAccountId));
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
