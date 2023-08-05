package com.griotold.bankshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.dto.item.ItemReqDto;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.*;

import static com.griotold.bankshop.dto.item.ItemReqDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ItemControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        User customer = userRepository.save(newUser("customer", "고객"));
        User admin = userRepository.save(newAdminUser("admin", "관리자"));
    }
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("registerItem() 테스트")
    void registerItem_test() throws Exception{
        // given
        ItemRegisterReqDto itemRegisterReqDto = new ItemRegisterReqDto();
        itemRegisterReqDto.setItemName("극세사 안경 닦기");
        itemRegisterReqDto.setPrice(10000);
        itemRegisterReqDto.setItemDetail("재질이 극세사인 잘 닦이는 안경닦기");
        itemRegisterReqDto.setStockNumber(100);
        String requestBody = om.writeValueAsString(itemRegisterReqDto);
        System.out.println("requestBody = " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/item")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "customer", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("고객은 아이템 등록 불가")
    void registerItem_customer_test() throws Exception{
        // given
        ItemRegisterReqDto itemRegisterReqDto = new ItemRegisterReqDto();
        itemRegisterReqDto.setItemName("극세사 안경 닦기");
        itemRegisterReqDto.setPrice(10000);
        itemRegisterReqDto.setItemDetail("재질이 극세사인 잘 닦이는 안경닦기");
        itemRegisterReqDto.setStockNumber(100);
        String requestBody = om.writeValueAsString(itemRegisterReqDto);
        System.out.println("requestBody = " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/item")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isForbidden());
    }

}