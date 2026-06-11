package org.example.repository;

import org.example.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByFromAccountId(long fromAccountId);
    List<TransactionLog> findByToAccountId(long toAccountId);
    List<TransactionLog> findByFromAccountIdOrToAccountId(long fromAccountId, long toAccountId);
    Optional<TransactionLog> findByIdempotencyKey(String idempotencyKey);
}

