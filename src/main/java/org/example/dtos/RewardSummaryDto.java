package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RewardSummaryDto {
    private long accountId;
    private int totalPoints;
    private List<RewardEntryDto> history;
}
