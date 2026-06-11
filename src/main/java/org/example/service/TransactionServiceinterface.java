package org.example.service;

import org.example.dtos.TransferDto;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionServiceinterface {
    @Transactional
    void transfer(TransferDto transferDto);


}
