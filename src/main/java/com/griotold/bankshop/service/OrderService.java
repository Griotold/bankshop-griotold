package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.order.Order;
import com.griotold.bankshop.domain.order.OrderRepository;
import com.griotold.bankshop.domain.orderItem.OrderItem;
import com.griotold.bankshop.domain.orderItem.OrderItemQueryRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static com.griotold.bankshop.dto.order.OrderReqDto.*;
import static com.griotold.bankshop.dto.order.OrderRespDto.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemQueryRepository orderItemQueryRepository;

    @Transactional
    public OrderReturnDto order(OrderDto orderDto, Long userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Item itemPS = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(() -> new CustomApiException("없는 상품입니다."));

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(itemPS, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(userPS, orderItemList);
        Order orderPS = orderRepository.save(order);

        return new OrderReturnDto(orderPS, orderItem);
    }

    public OrderHistDto historyList(Long userId, String orderStatus,
                                    Pageable pageable) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Page<OrderItem> orderItemPG
                = orderItemQueryRepository.findOrderItem(orderStatus, pageable);

        return new OrderHistDto(userPS, orderItemPG);


    }
}
