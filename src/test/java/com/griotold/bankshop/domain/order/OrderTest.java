package com.griotold.bankshop.domain.order;

import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.orderItem.OrderItem;
import com.griotold.bankshop.domain.orderItem.OrderItemRepository;
import com.griotold.bankshop.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderTest extends DummyObject {
    @Autowired
    EntityManager em;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderItemRepository orderItemRepository;


    @Test
    @DisplayName("영속성 전이 테스트")
    void cascade_test() throws Exception {
        // given
        Order order = new Order();

        for(int i = 0; i < 3; i++){
            Item item = newItem("아이템" + i);
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        // when
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(savedOrder.getOrderItems().size())
                .isEqualTo(3);
    }
    @Test
    @DisplayName("DummyObject.newOrderItem 테스트")
    void newOrderItem_test() throws Exception {
        // given
        Order order = orderSetting();

        orderRepository.saveAndFlush(order);
        em.clear();

        // when
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(savedOrder.getOrderItems().size())
                .isEqualTo(3);
    }

    @Test
    @DisplayName("고아객체 제거 ")
    void orphan_test() throws Exception {
        // given
        Order order = new Order();

        for(int i = 0; i < 3; i++){
            Item item = newItem("아이템" + i);
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.save(order);

        // when
        order.getOrderItems().remove(0);
        em.flush();

        Order findedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        // then
        assertThat(findedOrder.getOrderItems().size()).isEqualTo(2);
    }
    @Test
    @DisplayName("고아 객체 제거 - newOrderItem 활용")
    void orphan_newOrderItem_test() throws Exception {
        // given
        Order order = orderSetting();
        orderRepository.save(order);

        // when
        order.getOrderItems().remove(0);
        em.flush();

        Order findedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        // then
        assertThat(findedOrder.getOrderItems().size()).isEqualTo(2);

    }
    @Test
    @DisplayName("지연로딩 테스트")
    void lazy_test() throws Exception {
        // given
        Order order = orderSetting();
        orderRepository.save(order);
        Long orderItemId = order.getOrderItems().get(0).getId();

        em.flush();
        em.clear();

        // when
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        Order orderProxy = orderItem.getOrder();
        System.out.println("orderProxy.getClass() = " + orderProxy.getClass());
        System.out.println("===============");
        orderProxy.getCreatedAt();
        System.out.println("===============");
        // then
    }
    @Test
    @DisplayName("createOrder() 테스트")
    void createOrder_test() throws Exception {
        // given
        User user = newUser("griotold", "고리오영감");
        Item item = newItem("book");
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, 20);
        orderItemList.add(orderItem);

        // when
        Order order = Order.createOrder(user, orderItemList);

        // then
        assertThat(order.getUser().getUsername()).isEqualTo("griotold");
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        assertThat(order.getOrderItems().get(0).getTotalPrice()).isEqualTo(200000);
        assertThat(orderItem.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("getTotalPrice() 테스트")
    void getTotalPrice_test() throws Exception {
        // given
        User user = newUser("griotold", "고리오영감");
        Item book = newItem("book");
        Item pencil = newItem("pencil");

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItemBook = OrderItem.createOrderItem(book, 20);
        OrderItem orderItemPencil = OrderItem.createOrderItem(pencil, 30);
        orderItemList.add(orderItemBook);
        orderItemList.add(orderItemPencil);

        Order order = Order.createOrder(user, orderItemList);

        // when
        int totalPrice = order.getTotalPrice().intValue();

        // then
        assertThat(totalPrice).isEqualTo(500000);
    }

    Order orderSetting() {
        Order order = new Order();

        Item item1 = newItem("츄르");
        Item item2 = newItem("물티슈");
        Item item3 = newItem("안경닦이");
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        newOrderItem(item1, order);
        newOrderItem(item2, order);
        newOrderItem(item3, order);
        return order;
    }

}