package com.griotold.bankshop.domain.transaction;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements Dao{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Transaction> findTransactionList(Long accountId, String transactionType, Integer page) {
        QTransaction transaction = new QTransaction("transaction");
        JPAQuery<Transaction> query = jpaQueryFactory.selectFrom(transaction);

        query.leftJoin(transaction.withdrawAccount).fetchJoin();
        query.leftJoin(transaction.depositAccount).fetchJoin();

        query.where(transactionTypeCheck(transactionType, accountId));
        query.limit(5).offset(page * 5);

        return query.fetch();
    }

    private BooleanExpression transactionTypeCheck(String transactionType, Long accountId) {
        QTransaction transaction = new QTransaction("transaction");

        if (TransactionType.valueOf(transactionType) == TransactionType.DEPOSIT) {
            return transaction.depositAccount.id.eq(accountId);
        } else if (TransactionType.valueOf(transactionType) == TransactionType.WITHDRAW) {
            return transaction.withdrawAccount.id.eq(accountId);
        } else {
            return transaction.withdrawAccount.id.eq(accountId).or(transaction.depositAccount.id.eq(accountId));
        }

    }
}
