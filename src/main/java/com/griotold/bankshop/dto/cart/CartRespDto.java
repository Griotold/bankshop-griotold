package com.griotold.bankshop.dto.cart;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.cart.Cart;
import com.griotold.bankshop.domain.cartItem.CartItem;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.order.Order;
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

    @Getter
    @Setter
    public static class CartItemUpdateRespDto {
        private Long cartItemId;
        private String itemName;
        private int count;
        private int orderPrice;

        public CartItemUpdateRespDto(CartItem cartItem) {
            this.cartItemId = cartItem.getId();
            this.itemName = cartItem.getItem().getItemName();
            this.count = cartItem.getCount();
            this.orderPrice = cartItem.getCount() * cartItem.getItem().getPrice();
        }
    }

    @Getter
    @Setter
    public static class CartOrderRespDto{
        private Long orderId;
        private Long accountNumber;
        private Long balance;
        private Long totalPrice;

        public CartOrderRespDto(Order order, Account account) {
            this.orderId = order.getId();
            this.accountNumber = account.getNumber();
            this.balance = account.getBalance();
            this.totalPrice = order.getTotalPrice().longValue();
        }
    }
}
