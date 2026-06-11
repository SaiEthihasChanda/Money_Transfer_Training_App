package org.example.service;

import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.TransactionStatus;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component("TransactionService")
public class TransactionService implements TransactionServiceinterface {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final RewardServiceInterface rewardService;

    @Autowired
    public TransactionService(AccountRepository accountRepository,
                               TransactionRepository transactionRepository,
                               RewardServiceInterface rewardService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.rewardService = rewardService;
        logger.info("TransactionService initialized");
    }

    @Override
    @Transactional
    public void transfer(int from, int to, int amount) {
        Transaction transaction = new Transaction();
        transaction.setFromAccountId((long) from);
        transaction.setToAccountId((long) to);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());

        // Rule: self-transfer not allowed
        if (from == to) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setRemarks("Self-transfer not allowed");
            transactionRepository.save(transaction);
            logger.warn("Self-transfer attempted for account {}", from);
            return;
        }

        Account fromAccount = accountRepository.findById((long) from)
                .orElseThrow(() -> new RuntimeException("Account not found: " + from));

        // Verify receiver exists before moving any money
        accountRepository.findById((long) to)
                .orElseThrow(() -> new RuntimeException("Account not found: " + to));

        if (fromAccount.getBalance() >= amount) {
            credit(to, amount);
            debit(from, amount);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setRemarks("Transfer successful");
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setRemarks("Insufficient balance");
            logger.error("Insufficient balance in account {} for transfer of ₹{}", from, amount);
        }

        Transaction saved = transactionRepository.save(transaction);

        if (saved.getStatus() == TransactionStatus.SUCCESS) {
            rewardService.evaluateAndGrant(saved);
        }
    }

    @Override
    @Transactional
    public void credit(int id, int amount) {
        Account account = accountRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void debit(int id, int amount) {
        Account account = accountRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public int getBalance(int id) {
        Account account = accountRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        return account.getBalance();
    }
}
