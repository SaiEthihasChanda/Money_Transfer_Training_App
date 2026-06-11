package org.example.service;

import org.example.entity.RewardLedger;
import org.example.entity.TransactionLog;

import java.util.List;

public interface RewardServiceInterface {
    void evaluateAndGrant(TransactionLog transactionLog);
    int getRewardBalance(long accountId);
    List<RewardLedger> getRewardHistory(long accountId);
}
