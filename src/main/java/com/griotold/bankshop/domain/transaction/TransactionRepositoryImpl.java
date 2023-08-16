package com.griotold.bankshop.domain.transaction;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.griotold.bankshop.domain.transaction.QTransaction.transaction;
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements Dao{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Transaction> findTransactionList(Long accountId, String transactionType, Pageable pageable) {
        JPAQuery<Transaction> query = queryFactory.selectFrom(transaction);

        query.leftJoin(transaction.withdrawAccount).fetchJoin();
        query.leftJoin(transaction.depositAccount).fetchJoin();

        query.where(transactionTypeCheck(transactionType, accountId));
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        List<Transaction> content = query.fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(transaction.count())
                        .from(transaction)
                        .where(transactionTypeCheck(transactionType, accountId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression transactionTypeCheck(String transactionType, Long accountId) {

        if (TransactionType.valueOf(transactionType) == TransactionType.DEPOSIT) {
            return transaction.depositAccount.id.eq(accountId);
        } else if (TransactionType.valueOf(transactionType) == TransactionType.WITHDRAW) {
            return transaction.withdrawAccount.id.eq(accountId);
        } else {
            return transaction.withdrawAccount.id.eq(accountId).or(transaction.depositAccount.id.eq(accountId));
        }
    }

}
