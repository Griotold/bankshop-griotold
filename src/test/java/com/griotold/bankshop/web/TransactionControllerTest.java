package com.griotold.bankshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.transaction.TransactionRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TransactionControllerTest extends DummyObject {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
        em.clear();
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("입출금 목록 보기")
    void transactionList_test() throws Exception{
        // given
        Long number = 1111L;
        String transactionType = "ALL";
        String page = "0";

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/accounts/" + number + "/transactions")
                .param("transactionType", transactionType)
                .param("page", page));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.msg").value("입출금 목록 보기 성공"));

    }
    @WithUserDetails(value = "kandela", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("다른 사람이 계좌 내역을 볼 경우")
    void transactionList_another_user_test() throws Exception{
        // given
        Long number = 1111L;
        String transactionType = "ALL";
        String page = "0";

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/accounts/" + number + "/transactions")
                .param("transactionType", transactionType)
                .param("page", page));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 소유자가 아닙니다."));
    }

    private void dataSetting() {
        User griotold = userRepository.save(newUser("griotold", "고리오영감"));
        User kandela = userRepository.save(newUser("kandela", "칸델라"));
        User rien = userRepository.save(newUser("rien", "리앵"));
        User admin = userRepository.save(newUser("admin", "관리자"));

        Account griotoldAccount1 = accountRepository.save(newAccount(1111L, griotold));
        Account kandelaAccount1 = accountRepository.save(newAccount(2222L, kandela));
        Account rienAccount1 = accountRepository.save(newAccount(3333L, rien));
        Account griotoldAccount2 = accountRepository.save(newAccount(4444L, griotold));

        transactionRepository.save(newWithdrawTransaction(griotoldAccount1, accountRepository));
        transactionRepository.save(newDepositTransaction(kandelaAccount1, accountRepository));
        transactionRepository.save(newTransferTransaction(griotoldAccount1, kandelaAccount1, accountRepository));
        transactionRepository.save(newTransferTransaction(griotoldAccount1, rienAccount1, accountRepository));
        transactionRepository.save(newTransferTransaction(kandelaAccount1, griotoldAccount1, accountRepository));
    }

}