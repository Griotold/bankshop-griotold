package com.griotold.bankshop.dto.order;

import com.griotold.bankshop.domain.item.ItemImg;
import com.griotold.bankshop.domain.order.Order;
import com.griotold.bankshop.domain.orderItem.OrderItem;
import com.griotold.bankshop.utils.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRespDto {

    @Getter
    @Setter
    public static class OrderHistDto {
        private Long orderId;
        private String orderDate;
        private String orderStatus;
        private List<OrderItemDto> orderItemDtos;

        public OrderHistDto(Order order, List<OrderItem> orderItems) {
            this.orderId = order.getId();
            this.orderDate = CustomDateUtil.toStringFormat(order.getCreatedAt());
            this.orderStatus = order.getOrderStatus().getValue();
            this.orderItemDtos =
                    orderItems.stream()
                            .map(OrderItemDto::new)
                            .collect(Collectors.toList());
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
}
