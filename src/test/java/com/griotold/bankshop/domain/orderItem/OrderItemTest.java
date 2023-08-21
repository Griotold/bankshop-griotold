package com.griotold.bankshop.domain.orderItem;

import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.item.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")

class OrderItemTest extends DummyObject {

    @Test
    @DisplayName("createOrderItems() 테스트")
    void createOrderItems_test() throws Exception {
        // given
        Item item = newItem("book");
        int count = 20;

        // when
        OrderItem orderItem = OrderItem.createOrderItem(item, count);

        // then
        assertThat(item.getStockNumber()).isEqualTo(80);
        assertThat(orderItem.getCount()).isEqualTo(20);
        assertThat(orderItem.getOrderPrice()).isEqualTo(10000);
        assertThat(orderItem.getTotalPrice()).isEqualTo(200000);
    }

}