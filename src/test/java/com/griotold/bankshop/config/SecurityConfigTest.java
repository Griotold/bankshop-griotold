//package com.griotold.bankshop.config;
//
//import com.griotold.bankshop.config.dummy.DummyObject;
//import com.griotold.bankshop.domain.user.User;
//import com.griotold.bankshop.domain.user.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.TestExecutionEvent;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
////@Sql("classpath:db/teardown.sql")
//
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//class SecurityConfigTest extends DummyObject {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @BeforeEach
//    void set() {
//        User test = userRepository.save(newUser("test", "고리오영감"));
//    }
//
//    @Test
//    @DisplayName("인증 테스트")
//    void authentication_test() throws Exception {
//        // given
//        // when
//        ResultActions resultActions = mvc.perform(get("/api/s/blahblah"));
//        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
//        int status = resultActions.andReturn().getResponse().getStatus();
//        System.out.println("responseBody = " + responseBody);
//
//        // then
//        assertThat(status).isEqualTo(401);
//
//    }
//    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    @Test
//    @DisplayName("인가 테스트")
//    void authorization_test() throws Exception {
//        // given
//
//        // when
//        ResultActions resultActions = mvc.perform(get("/api/admin/bahblahg"));
//        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
//        int status = resultActions.andReturn().getResponse().getStatus();
//
//        // then
//        assertThat(status).isEqualTo(403);
//
//    }
//
//}