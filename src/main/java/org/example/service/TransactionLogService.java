package org.example.service;

import org.example.dtos.TransactionLogDto;
import org.example.entity.TransactionLog;
import org.example.repository.TransactionLogRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("TransactionLogService")
public class TransactionLogService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("transaction-log-service");

    private final TransactionLogRepository transactionLogRepository;

    @Autowired
    public TransactionLogService(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
        logger.info("TransactionLogRepository injected into TransactionLogService");
        logger.info("TransactionLogService initialized");
    }

    @Transactional
    public List<TransactionLogDto> getTransactionsByFromAccount(long fromAccountId) {
        List<TransactionLog> transactions = transactionLogRepository.findByFromAccountId(fromAccountId);
        return transactions.stream().map(this::mapToDto).toList();
    }

    @Transactional
    public List<TransactionLogDto> getTransactionsByToAccount(long toAccountId) {
        List<TransactionLog> transactions = transactionLogRepository.findByToAccountId(toAccountId);
        return transactions.stream().map(this::mapToDto).toList();
    }

    @Transactional
    public List<TransactionLogDto> getAccountTransactionHistory(long accountId) {
        List<TransactionLog> transactions = transactionLogRepository.findByFromAccountIdOrToAccountId(accountId, accountId);
        return transactions.stream().map(this::mapToDto).toList();
    }

    @Transactional
    public TransactionLogDto getTransactionById(long id) {
        TransactionLog transaction = transactionLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapToDto(transaction);
    }

    private TransactionLogDto mapToDto(TransactionLog transactionLog) {
        return new TransactionLogDto(
            transactionLog.getId(),
            transactionLog.getFromAccountId(),
            transactionLog.getToAccountId(),
            transactionLog.getAmount(),
            transactionLog.getStatus(),
            transactionLog.getFailureReason(),
            transactionLog.getIdempotencyKey(),
            transactionLog.getCreatedOn()
        );
    }
}

