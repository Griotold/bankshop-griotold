package com.griotold.bankshop.domain.cartItem;

import com.griotold.bankshop.domain.item.QItem;
import com.griotold.bankshop.support.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import static com.griotold.bankshop.domain.cartItem.QCartItem.*;
import static com.griotold.bankshop.domain.item.QItem.*;

@Repository
public class CartItemQueryRepository extends Querydsl4RepositorySupport {
    public CartItemQueryRepository() {
        super(CartItem.class);
    }

    public Page<CartItem> findCartItem(Pageable pageable) {
        return applyPagination5(pageable, query ->
                query.selectFrom(cartItem)
                        .innerJoin(cartItem.item, item).fetchJoin()
                ,countQuery -> countQuery.select(cartItem.count())
                        .from(cartItem));
    }
}
