package com.griotold.bankshop.domain.orderItem;


import com.griotold.bankshop.domain.item.QItem;
import com.griotold.bankshop.domain.order.OrderStatus;
import com.griotold.bankshop.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.griotold.bankshop.domain.item.QItem.*;
import static com.griotold.bankshop.domain.order.QOrder.order;
import static com.griotold.bankshop.domain.orderItem.QOrderItem.orderItem;

@Repository
public class OrderItemQueryRepository extends Querydsl4RepositorySupport {
    public OrderItemQueryRepository() {
        super(OrderItem.class);
    }

    public Page<OrderItem> findOrderItem(String orderStatus, Pageable pageable) {

        return applyPagination5(pageable, query ->
                        query.selectFrom(orderItem)
                                .leftJoin(orderItem.order, order).fetchJoin()
                                .leftJoin(orderItem.item, item).fetchJoin()
                                .where(statusCheck(orderStatus)),
                countQuery ->
                        countQuery.select(orderItem.count())
                                .from(orderItem)
                                .leftJoin(orderItem.order, order)
                                .where(statusCheck(orderStatus)));
    }

    private BooleanExpression statusCheck(String orderStatus) {

        if (OrderStatus.valueOf(orderStatus) == OrderStatus.ORDER) {
            return orderItem.order.orderStatus.eq(OrderStatus.ORDER);
        } else if (OrderStatus.valueOf(orderStatus) == OrderStatus.CANCEL) {
            return orderItem.order.orderStatus.eq(OrderStatus.CANCEL);
        } else {
            return null;
        }

    }
}

