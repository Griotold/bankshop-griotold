package com.griotold.bankshop.domain.transaction;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements Dao{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Transaction> findTransactionList(Long accountId, String transactionType, Integer page) {
        QTransaction transaction = new QTransaction("transaction");
        jpaQueryFactory.selectFrom(transaction);

        return null;
    }
}
