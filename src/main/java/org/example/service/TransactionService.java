package org.example.service;

import org.example.dtos.TransferDto;
import org.example.entity.Account;
import org.example.entity.TransactionLog;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component("TransactionService")
public class TransactionService implements TransactionServiceinterface {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final RewardServiceInterface rewardService;

    @Autowired
    public TransactionService(AccountRepository accountRepository,
                               TransactionLogRepository transactionLogRepository,
                               RewardServiceInterface rewardService) {
        this.accountRepository = accountRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.rewardService = rewardService;
        logger.info("TransactionService initialized");
    }

    @Override
    @Transactional
    public void transfer(TransferDto transferDto) {
        String idempotencyKey = UUID.randomUUID().toString();
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setFromAccountId(transferDto.getFromAccountId());
        transactionLog.setToAccountId(transferDto.getToAccountId());
        transactionLog.setAmount(transferDto.getAmount());
        transactionLog.setIdempotencyKey(idempotencyKey);

        // Self-transfer guard — fail fast before touching balances
        if (transferDto.getFromAccountId() == transferDto.getToAccountId()) {
            transactionLog.setStatus("FAILED");
            transactionLog.setFailureReason("Self-transfer not allowed");
            transactionLogRepository.save(transactionLog);
            throw new RuntimeException("Self-transfer not allowed");
        }

        try {
            Account fromAccount = accountRepository.findById(transferDto.getFromAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found: " + transferDto.getFromAccountId()));
            Account toAccount = accountRepository.findById(transferDto.getToAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found: " + transferDto.getToAccountId()));

            if (fromAccount.getBalance() >= transferDto.getAmount()) {
                toAccount.credit(transferDto.getAmount());
                fromAccount.debit(transferDto.getAmount());
                accountRepository.save(toAccount);
                accountRepository.save(fromAccount);

                transactionLog.setStatus("SUCCESS");
                TransactionLog saved = transactionLogRepository.save(transactionLog);

                rewardService.evaluateAndGrant(saved);

                logger.info("Transfer successful from account {} to account {}",
                        fromAccount.getId(), toAccount.getId());
            } else {
                transactionLog.setStatus("FAILED");
                transactionLog.setFailureReason("Insufficient balance");
                transactionLogRepository.save(transactionLog);

                logger.error("Transfer failed — insufficient balance in account {}", fromAccount.getId());
                throw new RuntimeException("Insufficient balance for transfer");
            }
        } catch (RuntimeException ex) {
            if (transactionLog.getStatus() == null) {
                transactionLog.setStatus("FAILED");
                transactionLog.setFailureReason(ex.getMessage());
                transactionLogRepository.save(transactionLog);
            }
            logger.error("Transfer failed: {}", ex.getMessage());
            throw ex;
        }
    }
}
