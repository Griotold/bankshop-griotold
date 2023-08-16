package com.griotold.bankshop.domain.transaction;

import com.griotold.bankshop.dto.transaction.TransactionSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Dao {
    Page<Transaction> findTransactionList(@Param("accountId") Long accountId,
                                          @Param("transactionType") String transactionType,
                                          @Param("pageable") Pageable pageable);

}
