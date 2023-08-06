package com.griotold.bankshop.dto.item;

import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemSellStatus;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ItemReqDto {


    @Getter
    @Setter
    public static class ItemRegisterReqDto {
        @NotEmpty
        private String itemName;

        @NotNull
        @Digits(integer = 6, fraction = 4)
        private int price;

        @NotNull
        @Digits(integer = 3, fraction = 4)
        private int stockNumber;

        @NotEmpty
        private String itemDetail;

        public Item toEntity() {
            return Item.builder()
                    .itemName(itemName)
                    .price(price)
                    .stockNumber(stockNumber)
                    .itemDetail(itemDetail)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();
        }

    }
}
