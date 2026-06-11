package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionLogDto {
    private long id;
    private long fromAccountId;
    private long toAccountId;
    private int amount;
    private String status;
    private String failureReason;
    private String idempotencyKey;
    private LocalDateTime createdOn;
}

