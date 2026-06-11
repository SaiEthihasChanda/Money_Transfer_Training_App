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
public class AccountDto {
    private long id;
    private String holderName;
    private String status;
    private LocalDateTime lastupdatedAt;
    private int balance;
}
