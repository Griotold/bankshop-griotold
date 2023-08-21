package com.griotold.bankshop.domain.item;

import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.handler.ex.CustomOutOfStockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
class ItemTest extends DummyObject {

    @Test
    @DisplayName("removeStock() 메소드 테스트")
    void removeStock_test() throws Exception {
        // given
        Item item = newItem("마우스");
        int stockNumber = 30;

        // when
        item.removeStock(30);

        // then
        assertThat(item.getStockNumber()).isEqualTo(70);
    }

    @Test
    @DisplayName("removeStock - 재고를 넘겼을 경우")
    void removeStock_fail_exception() throws Exception {
        // given
        Item item = newItem("마우스");
        int stockNumber = 150;

        // when
        // then
        assertThatThrownBy(() -> item.removeStock(stockNumber))
                .isInstanceOf(CustomOutOfStockException.class);
    }

}