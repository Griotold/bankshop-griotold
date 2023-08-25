package com.griotold.bankshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.dto.order.OrderReqDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.griotold.bankshop.dto.order.OrderReqDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class OrderControllerTest extends DummyObject {

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void set() {
        dataSet();
        em.clear();
    }
    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("주문 요청 테스트")
    void orderRequest_test() throws Exception {
        // given
        OrderDto orderDto = new OrderDto();
        Item item = itemRepository.findByItemName("츄르").get();
        orderDto.setAccountNumber(1111L);
        orderDto.setAccountPassword(1234L);
        orderDto.setItemId(item.getId());
        orderDto.setCount(3);
        String requestBody = om.writeValueAsString(orderDto);
        log.debug("테스트 : {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/orders")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : {}", responseBody);


        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.msg").value("주문 완료"));
        resultActions.andExpect(jsonPath("$.data.orderStatus").value("주문"));
    }
    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("0개를 주문한 경우")
    void below_minimum_test() throws Exception {
        // given
        OrderDto orderDto = new OrderDto();
        Item item = itemRepository.findByItemName("츄르").get();
        orderDto.setItemId(item.getId());
        orderDto.setCount(0);
        String requestBody = om.writeValueAsString(orderDto);
        log.debug("테스트 : {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/orders")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions
                .andExpect(jsonPath("$.data.count")
                        .value("최소 주문 수량은 1개 입니다."));
    }
    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("1000개를 주문한 경우")
    void over_maximum_test() throws Exception {
        // given
        OrderDto orderDto = new OrderDto();
        Item item = itemRepository.findByItemName("츄르").get();
        orderDto.setItemId(item.getId());
        orderDto.setCount(1000);
        String requestBody = om.writeValueAsString(orderDto);
        log.debug("테스트 : {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/orders")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions
                .andExpect(jsonPath("$.data.count")
                        .value("최대 주문 수량은 999개 입니다."));
    }

    private void dataSet() {
        User griotold = userRepository.save(newUser("griotold", "고리오영감"));

        Account account = accountRepository.save(newAccount4Order(1111L, griotold));

        Item snack4Cat = itemRepository.save(newItem("츄르"));
        Item sweeper = itemRepository.save(newItem("안경닦이"));
        Item tissue = itemRepository.save(newItem("물티슈"));
    }

}