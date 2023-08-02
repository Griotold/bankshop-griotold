package com.griotold.bankshop.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("인증 테스트")
    void authentication_test() throws Exception {
        // given
        // when
        ResultActions resultActions = mvc.perform(get("/api/s/blahblah"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int status = resultActions.andReturn().getResponse().getStatus();

        // then
        assertThat(jsonPath("$.msg").value("로그인을 해주세요."));
        assertThat(status).isEqualTo(401);

    }

    @Test
    @DisplayName("인가 테스트")
    void authorization_test() throws Exception {
        // given

        // when

        ResultActions resultActions = mvc.perform(get("/api/admin/bahblahg"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int status = resultActions.andReturn().getResponse().getStatus();

        // then
        assertThat(jsonPath("$.msg").value("권한이 없습니다."));
        assertThat(status).isEqualTo(403);
    }

}