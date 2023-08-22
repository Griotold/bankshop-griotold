package com.griotold.bankshop.dto.cart;

import com.griotold.bankshop.domain.cart.Cart;
import com.griotold.bankshop.domain.cartItem.CartItem;
import com.griotold.bankshop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

public class CartRespDto {


    @Getter
    @Setter
    public static class CartAddRespDto {
        private Long itemId;
        private String itemName;
        private int count;
        private int OrderPrice;

        public CartAddRespDto(Item item, CartItem cartItem) {
            this.itemId = item.getId();
            this.itemName = item.getItemName();
            this.count = cartItem.getCount();
            this.OrderPrice = item.getPrice() * cartItem.getCount();
        }
    }
}
