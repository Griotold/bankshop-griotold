package com.griotold.bankshop.dto.order;

import com.griotold.bankshop.domain.order.Order;
import com.griotold.bankshop.domain.orderItem.OrderItem;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.utils.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

public class OrderRespDto {

    @Getter
    @Setter
    public static class OrderReturnDto {
        private Long orderId;
        private String orderDate;
        private String orderStatus;
        private OrderItemDto orderItemDto;

        public OrderReturnDto(Order order, OrderItem orderItem) {
            this.orderId = order.getId();
            this.orderDate = CustomDateUtil.toStringFormat(order.getCreatedAt());
            this.orderStatus = order.getOrderStatus().getValue();
            this.orderItemDto = new OrderItemDto(orderItem);
        }

        @Getter
        @Setter
        public static class OrderItemDto {
            private Long itemId;
            private String itemName;
            private int count;
            private int totalPrice;

            public OrderItemDto(OrderItem orderItem) {
                this.itemId = orderItem.getItem().getId();
                this.itemName = orderItem.getItem().getItemName();
                this.count = orderItem.getCount();
                this.totalPrice = orderItem.getTotalPrice();
            }
        }
    }

    @Getter
    @Setter
    public static class OrderHistDto {
        private Long userId;
        private String username;

        private Page<OrderItemDto> orderItemDtos;

        public OrderHistDto(User user, Page<OrderItem> orderItems) {
            this.userId = user.getId();
            this.username = user.getUsername();

            this.orderItemDtos =
                    orderItems.
                            map(OrderItemDto::new)
                            ;
        }

        @Getter
        @Setter
        public static class OrderItemDto {
            private Long orderId;
            private Long itemId;
            private String itemName;
            private int count;
            private int totalPrice;

            public OrderItemDto(OrderItem orderItem) {
                this.orderId = orderItem.getOrder().getId();
                this.itemId = orderItem.getItem().getId();
                this.itemName = orderItem.getItem().getItemName();
                this.count = orderItem.getCount();
                this.totalPrice = orderItem.getTotalPrice();
            }
        }
    }
}
