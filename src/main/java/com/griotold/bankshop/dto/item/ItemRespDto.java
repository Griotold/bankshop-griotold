package com.griotold.bankshop.dto.item;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemImg;
import com.griotold.bankshop.domain.item.ItemSellStatus;
import com.griotold.bankshop.domain.transaction.Transaction;
import com.griotold.bankshop.dto.transaction.TransactionRespDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRespDto {

    @Getter
    @Setter
    public static class ItemList4AdminDto{
        private Page<ItemImgDto> itemDtos;

        public ItemList4AdminDto(Page<ItemImg> itemImgs) {
            this.itemDtos = itemImgs
                    .map((itemImg) -> new ItemImgDto(itemImg));
        }

        @Getter
        @Setter
        public static class ItemImgDto {
            private Long id;
            private String itemName;
            private int price;
            private int stockNumber;
            private String itemSellStatus;
            private String imgName;
            private String oriImgName;
            private String imgUrl;

            public ItemImgDto(ItemImg itemImg) {
                this.id = itemImg.getItem().getId();
                this.itemName = itemImg.getItem().getItemName();
                this.price = itemImg.getItem().getPrice();
                this.stockNumber = itemImg.getItem().getStockNumber();
                this.itemSellStatus = itemImg.getItem().getItemSellStatus().name();
                this.imgName = itemImg.getImgName();
                this.oriImgName = itemImg.getOriImgName();
                this.imgUrl = itemImg.getImgUrl();
            }
        }
    }

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
    @Getter
    @Setter
    public static class ItemEditRespDto {
        private Long id;
        private String itemName;
        private int price;
        private int stockNumber;
        private String itemDetail;
        private String itemSellStatus;
        private Long itemImgId;
        private String imgName;
        private String oriImgName;
        private String imgUrl;

        public ItemEditRespDto(Item item, ItemImg itemImg) {
            this.id = item.getId();
            this.itemName = item.getItemName();
            this.price = item.getPrice();
            this.stockNumber = item.getStockNumber();
            this.itemDetail = item.getItemDetail();
            this.itemSellStatus = item.getItemSellStatus().toString();
            this.itemImgId = itemImg.getId();
            this.imgName = itemImg.getImgName();
            this.oriImgName = itemImg.getOriImgName();
            this.imgUrl = itemImg.getImgUrl();

        }
    }
}
