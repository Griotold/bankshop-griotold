package com.griotold.bankshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;

import static com.griotold.bankshop.dto.item.ItemReqDto.ItemRegisterReqDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Slf4j
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ItemControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @BeforeEach
    public void setUp() {
        User customer = userRepository.save(newUser("customer", "고객"));
        User admin = userRepository.save(newAdminUser("admin", "관리자"));

        Item item = itemRepository.save(newItem("츄르"));
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
        itemRegisterReqDto.setImgName("극세사 안경 닦기 이미지네임");
        itemRegisterReqDto.setOriImgName("원본 이미지네임");
        itemRegisterReqDto.setImgUrl("www.eyeglasses.com");
        String requestBody = om.writeValueAsString(itemRegisterReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/items")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

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

        // when
        ResultActions resultActions = mvc.perform(post("/api/admin/items")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        // then
        resultActions.andExpect(status().isForbidden());
    }
    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("상품 삭제 정상 테스트")
    void deleteItem_test() throws Exception{
        // given
        long itemId = 1L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/admin/items/" + itemId));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("상품 삭제 실패 테스트")
    void deleteItem_fail_test() throws Exception{
        // given
        long itemId = 2L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/admin/items/" + itemId));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @WithUserDetails(value = "customer", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("상품 삭제 권한 테스트")
    void deleteItem_customer() throws Exception{
        // given
        long itemId = 1L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/admin/items/" + itemId));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        // then
        resultActions.andExpect(status().isForbidden());
    }

}