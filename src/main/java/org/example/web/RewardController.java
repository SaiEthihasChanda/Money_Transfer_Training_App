package org.example.web;

import org.example.dtos.RewardEntryDto;
import org.example.dtos.RewardSummaryDto;
import org.example.entity.RewardLedger;
import org.example.service.RewardServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardController {

    @Autowired
    private RewardServiceInterface rewardService;

    @GetMapping("/{accountId}/summary")
    public ResponseEntity<?> getRewardSummary(@PathVariable long accountId) {
        try {
            int totalPoints = rewardService.getRewardBalance(accountId);
            List<RewardLedger> history = rewardService.getRewardHistory(accountId);

            List<RewardEntryDto> historyDtos = history.stream()
                    .map(e -> new RewardEntryDto(
                            e.getId(),
                            e.getTransactionLogId(),
                            e.getPointsAwarded(),
                            e.getDescription(),
                            e.getCreatedAt()))
                    .toList();

            return ResponseEntity.ok(new RewardSummaryDto(accountId, totalPoints, historyDtos));
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
