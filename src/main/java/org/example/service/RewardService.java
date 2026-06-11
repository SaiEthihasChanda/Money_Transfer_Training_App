package org.example.service;

import org.example.entity.RewardLedger;
import org.example.entity.Transaction;
import org.example.entity.TransactionStatus;
import org.example.repository.RewardLedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component("RewardService")
public class RewardService implements RewardServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(RewardService.class);

    // Minimum amount (exclusive) required for reward eligibility
    private static final int REWARD_THRESHOLD = 100;
    // One point per this many rupees
    private static final int RUPEES_PER_POINT = 100;

    private final RewardLedgerRepository rewardLedgerRepository;

    @Autowired
    public RewardService(RewardLedgerRepository rewardLedgerRepository) {
        this.rewardLedgerRepository = rewardLedgerRepository;
    }

    @Override
    @Transactional
    public void evaluateAndGrant(Transaction transaction) {
        if (!isEligible(transaction)) {
            logger.info("Transaction {} is not eligible for rewards", transaction.getId());
            return;
        }

        int points = transaction.getAmount() / RUPEES_PER_POINT;

        RewardLedger entry = new RewardLedger();
        entry.setAccountId(transaction.getFromAccountId());
        entry.setTransactionId(transaction.getId());
        entry.setPointsAwarded(points);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setDescription("Earned " + points + " point(s) for transferring ₹" + transaction.getAmount()
                + " to account #" + transaction.getToAccountId());

        rewardLedgerRepository.save(entry);
        logger.info("Granted {} reward point(s) to account {} for transaction {}",
                points, transaction.getFromAccountId(), transaction.getId());
    }

    @Override
    public int getRewardBalance(long accountId) {
        return rewardLedgerRepository.sumPointsByAccountId(accountId);
    }

    @Override
    public List<RewardLedger> getRewardHistory(long accountId) {
        return rewardLedgerRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private boolean isEligible(Transaction transaction) {
        // Rule 1: must be a successful transaction
        if (transaction.getStatus() != TransactionStatus.SUCCESS) {
            return false;
        }
        // Rule 2: amount must be greater than ₹100
        if (transaction.getAmount() <= REWARD_THRESHOLD) {
            return false;
        }
        // Rules 3 & 4: sender and receiver must be different accounts
        if (transaction.getFromAccountId().equals(transaction.getToAccountId())) {
            return false;
        }
        return true;
    }
}
