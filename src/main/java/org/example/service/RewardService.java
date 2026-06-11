package org.example.service;

import org.example.entity.RewardLedger;
import org.example.entity.TransactionLog;
import org.example.repository.RewardLedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("RewardService")
public class RewardService implements RewardServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(RewardService.class);

    private static final int REWARD_THRESHOLD = 100;
    private static final int RUPEES_PER_POINT = 100;

    private final RewardLedgerRepository rewardLedgerRepository;

    @Autowired
    public RewardService(RewardLedgerRepository rewardLedgerRepository) {
        this.rewardLedgerRepository = rewardLedgerRepository;
    }

    @Override
    @Transactional
    public void evaluateAndGrant(TransactionLog transactionLog) {
        if (!isEligible(transactionLog)) {
            logger.info("Transaction {} is not eligible for rewards", transactionLog.getId());
            return;
        }

        int points = transactionLog.getAmount() / RUPEES_PER_POINT;

        RewardLedger entry = new RewardLedger();
        entry.setAccountId(transactionLog.getFromAccountId());
        entry.setTransactionLogId(transactionLog.getId());
        entry.setPointsAwarded(points);
        entry.setDescription("Earned " + points + " point(s) for transferring ₹"
                + transactionLog.getAmount() + " to account #" + transactionLog.getToAccountId());

        rewardLedgerRepository.save(entry);
        logger.info("Granted {} reward point(s) to account {} for transaction {}",
                points, transactionLog.getFromAccountId(), transactionLog.getId());
    }

    @Override
    public int getRewardBalance(long accountId) {
        return rewardLedgerRepository.sumPointsByAccountId(accountId);
    }

    @Override
    public List<RewardLedger> getRewardHistory(long accountId) {
        return rewardLedgerRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    private boolean isEligible(TransactionLog transactionLog) {
        if (!"SUCCESS".equals(transactionLog.getStatus())) return false;
        if (transactionLog.getAmount() <= REWARD_THRESHOLD) return false;
        if (transactionLog.getFromAccountId() == transactionLog.getToAccountId()) return false;
        return true;
    }
}
