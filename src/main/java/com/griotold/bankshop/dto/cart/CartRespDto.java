package com.griotold.bankshop.dto.cart;

import com.griotold.bankshop.domain.cart.Cart;
import com.griotold.bankshop.domain.cartItem.CartItem;
import com.griotold.bankshop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

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

    @Getter
    @Setter
    public static class CartDetailRespDto {

        private Page<CartItemDto> cartItems;

        public CartDetailRespDto(Page<CartItem> cartItems){
            this.cartItems = cartItems.map(CartItemDto::new);
        }

        @Getter
        @Setter
        public static class CartItemDto {
            private Long cartItemId;
            private String itemName;
            private int price;
            private int count;
            private int orderPrice;

            public CartItemDto(CartItem cartItem) {
                this.cartItemId = cartItem.getId();
                this.itemName = cartItem.getItem().getItemName();
                this.price = cartItem.getItem().getPrice();
                this.count = cartItem.getCount();
                this.orderPrice = cartItem.getItem().getPrice() * cartItem.getCount();
            }
        }

    }
}
