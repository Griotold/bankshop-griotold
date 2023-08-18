package com.griotold.bankshop.domain.item;

import com.griotold.bankshop.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.griotold.bankshop.domain.item.QItem.item;
import static com.griotold.bankshop.domain.item.QItemImg.itemImg;

@Repository
public class ItemImgQueryRepository extends Querydsl4RepositorySupport {
    public ItemImgQueryRepository() {
        super(ItemImg.class);
    }
    public Page<ItemImg> findAllPage(String itemSellStatus, Pageable pageable) {
        return applyPagination5(pageable, query ->
                        query.selectFrom(itemImg)
                                .leftJoin(itemImg.item, item)
                                .where(statusCheck(itemSellStatus)),
                countQuery ->
                        countQuery.select(itemImg.count())
                                .from(itemImg)
                                .leftJoin(itemImg.item, item)
                                .where(statusCheck(itemSellStatus)));
    }

    private BooleanExpression statusCheck(String itemSellStatus) {

        if (ItemSellStatus.valueOf(itemSellStatus) == ItemSellStatus.SELL) {
            return itemImg.item.itemSellStatus.eq(ItemSellStatus.SELL);
        } else if (ItemSellStatus.valueOf(itemSellStatus) == ItemSellStatus.SOLD_OUT) {
            return itemImg.item.itemSellStatus.eq(ItemSellStatus.SOLD_OUT);
        } else {
            return null;
        }

    }

}
