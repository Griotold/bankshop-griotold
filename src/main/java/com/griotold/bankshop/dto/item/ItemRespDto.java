package com.griotold.bankshop.dto.item;

import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemImg;
import com.griotold.bankshop.domain.item.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRespDto {

    @Getter
    @Setter

    public static class ItemIdRespDto {
        private Long id;
        private String itemName;
        private int price;
        private int stockNumber;
        private String itemSellStatus;

        public ItemIdRespDto(Item item) {
            this.id = item.getId();
            this.itemName = item.getItemName();
            this.price = item.getPrice();
            this.stockNumber = item.getStockNumber();
            this.itemSellStatus = item.getItemSellStatus().name();
        }
    }

    @Getter
    @Setter
    public static class ItemListRespDto {
        private List<ItemDto> itemDtos = new ArrayList<>();

        public ItemListRespDto(List<Item> items) {
            this.itemDtos = items.stream().map((ItemDto::new)).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class ItemDto {
            private Long id;
            private String itemName;
            private int price;
            private int stockNumber;
            private String itemSellStatus;

            public ItemDto(Item item) {
                this.id = item.getId();
                this.itemName = item.getItemName();
                this.price = item.getPrice();
                this.stockNumber = item.getStockNumber();
                this.itemSellStatus = item.getItemSellStatus().name();
            }
        }
    }

    @Getter
    @Setter
    public static class ItemRegisterRespDto {
        private Long id;
        private String itemName;
        private int price;
        private int stockNumber;
        private String itemSellStatus;
        private Long itemImgId;

        public ItemRegisterRespDto(Item item, ItemImg itemImg) {
            this.id = item.getId();
            this.itemName = item.getItemName();
            this.price = item.getPrice();
            this.stockNumber = item.getStockNumber();
            this.itemSellStatus = item.getItemSellStatus().toString();
            this.itemImgId = itemImg.getId();

        }
    }
}
