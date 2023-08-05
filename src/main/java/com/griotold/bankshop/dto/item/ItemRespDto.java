package com.griotold.bankshop.dto.item;

import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

public class ItemRespDto {

    @Getter
    @Setter
    public static class ItemRegisterRespDto {
        private Long id;
        private String itemName;
        private int price;
        private int stockNumber;
        private String itemSellStatus;

        public ItemRegisterRespDto(Item item) {
            this.id = item.getId();
            this.itemName = item.getItemName();
            this.price = item.getPrice();
            this.stockNumber = item.getStockNumber();
            this.itemSellStatus = getItemSellStatus();
        }
    }
}
