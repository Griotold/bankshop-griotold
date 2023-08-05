package com.griotold.bankshop.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.griotold.bankshop.dto.user.UserReqDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper om;
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(newUser("griotold", "고리오영감"));
    }
    
    @Test
    @DisplayName("인증 성공 테스트")
    void successfulAuthentication_test() throws Exception{
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("griotold");
        loginReqDto.setPassword("1234");
        String requestBody = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(jwtToken).isNotNull();
        assertThat(jwtToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
        resultActions.andExpect(jsonPath("$.data.username").value("griotold"));
    }
    @Test
    @DisplayName("인증 실패 테스트")
    void unsuccessfulAuthentication_test() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("griotold1");
        loginReqDto.setPassword("1234");
        String requestBody = om.writeValueAsString(loginReqDto);
        System.out.println("requestBody = " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);
        System.out.println("jwtToken = " + jwtToken);

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(jwtToken).isNull();
        resultActions.andExpect(jsonPath("$.msg").value("로그인 실패"));
    }
}