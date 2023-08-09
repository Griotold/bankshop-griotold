package com.griotold.bankshop.domain.transaction;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
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
        query.limit(3).offset(page * 3);

        return query.fetch();
    }

    private BooleanExpression transactionTypeCheck(String transactionType, Long accountId) {
        QTransaction transaction = new QTransaction("transaction");

        if (!StringUtils.hasText(transactionType)) {
            return transaction.withdrawAccount.id.eq(accountId).or(transaction.depositAccount.id.eq(accountId));
        } else if (TransactionType.valueOf(transactionType) == TransactionType.DEPOSIT) {
            return transaction.depositAccount.id.eq(accountId);
        } else if (TransactionType.valueOf(transactionType) == TransactionType.WITHDRAW) {
            return transaction.withdrawAccount.id.eq(accountId);
        } else {
            return null;
        }
    }
}
