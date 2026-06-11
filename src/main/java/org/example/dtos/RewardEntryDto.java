package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RewardEntryDto {
    private Long id;
    private Long transactionLogId;
    private int pointsAwarded;
    private String description;
    private LocalDateTime createdAt;
}
