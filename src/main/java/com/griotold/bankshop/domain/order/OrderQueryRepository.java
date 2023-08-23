package com.griotold.bankshop.domain.order;

import com.griotold.bankshop.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.griotold.bankshop.domain.order.QOrder.*;
import static com.griotold.bankshop.domain.orderItem.QOrderItem.orderItem;
import static com.griotold.bankshop.domain.user.QUser.user;

@Repository
public class OrderQueryRepository extends Querydsl4RepositorySupport {
    public OrderQueryRepository() {
        super(Order.class);
    }

    public Page<Order> findOrderHistory(String orderStatus, Pageable pageable) {
        return applyPagination5(pageable, query ->
                query.selectFrom(order)
                        .innerJoin(order.user, user).fetchJoin()
                        .where(statusCheck(orderStatus)),
                countQuery -> countQuery.select(order.count())
                        .from(order)
                        .where(statusCheck(orderStatus)));
    }

    private BooleanExpression statusCheck(String orderStatus) {

        if (OrderStatus.valueOf(orderStatus) == OrderStatus.ORDER) {
            return order.orderStatus.eq(OrderStatus.ORDER);
        } else if (OrderStatus.valueOf(orderStatus) == OrderStatus.CANCEL) {
            return order.orderStatus.eq(OrderStatus.CANCEL);
        } else {
            return null;
        }

    }
}
