package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.order.Order;
import com.griotold.bankshop.domain.order.OrderQueryRepository;
import com.griotold.bankshop.domain.order.OrderRepository;
import com.griotold.bankshop.domain.orderItem.OrderItem;
import com.griotold.bankshop.domain.orderItem.OrderItemQueryRepository;
import com.griotold.bankshop.domain.transaction.Transaction;
import com.griotold.bankshop.domain.transaction.TransactionRepository;
import com.griotold.bankshop.domain.transaction.TransactionType;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.dto.cart.CartRespDto;
import com.griotold.bankshop.handler.ex.CustomApiException;
import com.griotold.bankshop.ztudy.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.griotold.bankshop.dto.cart.CartRespDto.*;
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
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final OrderQueryRepository orderQueryRepository;

    @Transactional
    public OrderReturnDto order(OrderDto orderDto, Long userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Account accountPS = accountRepository.findByNumber(orderDto.getAccountNumber())
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        Item itemPS = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(() -> new CustomApiException("없는 상품입니다."));

        Long amount = Long.valueOf(itemPS.getPrice() * orderDto.getCount());

        accountPS.checkOwner(userId);

        accountPS.checkSamePassword(orderDto.getAccountPassword());

        accountPS.checkBalance(amount);

        accountPS.withdraw(amount);

        Transaction transaction = Transaction.createOrderTransaction(accountPS, amount);
        transactionRepository.save(transaction);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(itemPS, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(userPS, orderItemList);
        Order orderPS = orderRepository.save(order);

        return new OrderReturnDto(accountPS, orderPS, orderItem);
    }

    @Transactional
    public CartOrderRespDto orders(List<OrderCartDto> orderCartDtoList, Long userId,
                                               Long accountNumber, Long accountPassword) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Account accountPS = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        accountPS.checkSamePassword(accountPassword);

        Long amount = 0L;

        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderCartDto orderCartDto : orderCartDtoList) {
            Item itemPS = itemRepository.findById(orderCartDto.getItemId())
                    .orElseThrow(() -> new CustomApiException("상품을 찾을 수 없습니다."));

            amount += Long.valueOf(itemPS.getPrice() * orderCartDto.getCount());

            OrderItem orderItem = OrderItem.createOrderItem(itemPS, orderCartDto.getCount());
            orderItemList.add(orderItem);
        }

        accountPS.checkBalance(amount);

        accountPS.withdraw(amount);

        Transaction transaction = Transaction.createOrderTransaction(accountPS, amount);
        transactionRepository.save(transaction);

        Order order = Order.createOrder(userPS, orderItemList);
        orderRepository.save(order);

        return new CartOrderRespDto(order, accountPS);
    }

    @Transactional
    public void orderCancel(OrderCancelReqDto orderCancelReqDto, Long orderId, Long userId){
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Account accountPS = accountRepository.findByNumber(orderCancelReqDto.getAccountNumber())
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        Order orderPS = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomApiException("주문을 찾을 수 없습니다."));

        long returnAmount = orderPS.getTotalPrice().longValue();

        accountPS.checkOwner(userId);

        orderPS.checkOwner(userId);

        accountPS.deposit(returnAmount);

        Transaction transaction = Transaction.createdCancelTransaction(accountPS, returnAmount);
        transactionRepository.save(transaction);

        orderPS.cancelOrder();
    }

    public OrderHistDto historyList(Long userId, String orderStatus,
                                    Pageable pageable) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Page<OrderItem> orderItemPG
                = orderItemQueryRepository.findOrderItem(orderStatus, pageable);

        return new OrderHistDto(userPS, orderItemPG);
    }
    /**
     * 컬렉션 조회 최적화
     * */
    public OrderHistDtoV2 historyListV2(Long userId, String orderStatus,
                                    Pageable pageable) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Page<Order> orderPG
                = orderQueryRepository.findOrderHistory(orderStatus, pageable);

        return new OrderHistDtoV2(userPS, orderPG);
    }
}
