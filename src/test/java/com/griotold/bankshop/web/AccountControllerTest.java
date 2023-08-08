package com.griotold.bankshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
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

import static com.griotold.bankshop.dto.account.AccountReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {

        User griotold = userRepository.save(newUser("griotold", "고리오영감"));
        User kandela = userRepository.save(newUser("kandela", "칸델라"));
        User rien = userRepository.save(newUser("rien", "리앵"));

        Account griotoldAccount1 = accountRepository.save(newAccount(1111L, griotold));
        Account griotoldAccount2 = accountRepository.save(newAccount(2222L, griotold));
        Account kandelaAccount = accountRepository.save(newAccount(3333L, kandela));
        Account rienAccount = accountRepository.save(newAccount(4444L, rien));

    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("registerAccount() 테스트")
    void registerAccount_test() throws Exception {
        // given
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(9999L);
        accountSaveReqDto.setPassword(1234L);
        String requestBody = om.writeValueAsString(accountSaveReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("유효성 검증 - 4자리만 받는지")
    void registerAccount_validation_test() throws Exception {
        // given
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(99999L);
        accountSaveReqDto.setPassword(12345L);
        String requestBody = om.writeValueAsString(accountSaveReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("유효성 검사 실패"));
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("유저별 계좌 목록 조회")
    void retrieveUserAccountList_test() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/api/s/accounts/login-user"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("deleteAccount() 테스트")
    void deleteAccount_test() throws Exception {
        // given
        long number = 1111L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/s/accounts/" + number));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("다른 유저의 계좌를 삭제할 경우")
    void deleteAccount_another_user() throws Exception {
        // given
        long number = 3333L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/s/accounts/" + number));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 소유자가 아닙니다."));

    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("없는 계좌를 삭제할 경우")
    void deleteAccount_no_account() throws Exception {
        // given
        long number = 5555L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/s/accounts/" + number));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("계좌를 찾을 수 없습니다."));

    }

    @Test
    @DisplayName("계좌 입금 컨트롤러 테스트")
    void deposit_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setTransactionType("DEPOSIT");
        accountDepositReqDto.setTel("01034567890");

        String requestBody = om.writeValueAsString(accountDepositReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/accounts/deposit")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 입금 완료"));
    }

    @Test
    @DisplayName("계좌 입금 실패 - 0원")
    void deposit_no_money_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(0L);
        accountDepositReqDto.setTransactionType("DEPOSIT");
        accountDepositReqDto.setTel("01034567890");

        String requestBody = om.writeValueAsString(accountDepositReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/accounts/deposit")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("0원 이하의 금액을 입금할 수 없습니다."));
    }

    @Test
    @DisplayName("계좌 입금 실패 - 없는 계좌 번호")
    void deposit_no_account_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(5555L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setTransactionType("DEPOSIT");
        accountDepositReqDto.setTel("01034567890");

        String requestBody = om.writeValueAsString(accountDepositReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/accounts/deposit")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("계좌를 찾을 수 없습니다."));
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("계좌 출금 컨트롤러 테스트")
    void withdraw_test() throws Exception {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setTransactionType("WITHDRAW");

        String requestBody = om.writeValueAsString(accountWithdrawReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/withdraw")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 출금 완료"));
        resultActions.andExpect(jsonPath("$.data.balance").value(900L));
    }

    @WithUserDetails(value = "kandela", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("다른 사람이 계좌 출금 시도")
    void withdraw_another_user_test() throws Exception {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setTransactionType("WITHDRAW");

        String requestBody = om.writeValueAsString(accountWithdrawReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/withdraw")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 소유자가 아닙니다."));
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("0원 출금 시도")
    void withdraw_no_money_test() throws Exception {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setAmount(0L);
        accountWithdrawReqDto.setTransactionType("WITHDRAW");

        String requestBody = om.writeValueAsString(accountWithdrawReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/withdraw")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("0원 이하의 금액을 출금할 수 없습니다."));

    }
    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("계좌 패스워드가 다른 걸 넣었을 때")
    void withdraw_password_test() throws Exception {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setPassword(4444L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setTransactionType("WITHDRAW");

        String requestBody = om.writeValueAsString(accountWithdrawReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/withdraw")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 비밀번호 검증에 실패했습니다."));

    }
    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("거래 타입에 이상한 값을 넣었을 때")
    void withdraw_invalid_transactionType_test() throws Exception {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setTransactionType("BLAHBLAH");

        String requestBody = om.writeValueAsString(accountWithdrawReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/withdraw")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("유효성 검사 실패"));

    }
    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("계좌 이체 성공 테스트")
    void transfer_success_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(3333L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setTransactionType("TRANSFER");

        String requestBody = om.writeValueAsString(accountTransferReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/transfer")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 이체 완료"));
    }
    @WithUserDetails(value = "kandela", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("다른 사람이 계좌 이체할 때")
    void transfer_another_user_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(3333L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setTransactionType("TRANSFER");

        String requestBody = om.writeValueAsString(accountTransferReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/transfer")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 소유자가 아닙니다."));
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("출금 계좌와 입금 계좌가 같을 때")
    void transfer_withdraw_deposit_same_number_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(1111L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setTransactionType("TRANSFER");

        String requestBody = om.writeValueAsString(accountTransferReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/transfer")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("입출금 계좌가 동일할 수 없습니다."));
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("계좌 패스워드 검증")
    void transfer_invalid_password_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(3333L);
        accountTransferReqDto.setWithdrawPassword(9999L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setTransactionType("TRANSFER");

        String requestBody = om.writeValueAsString(accountTransferReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/transfer")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("계좌 비밀번호 검증에 실패했습니다."));
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("0원을 이체하려 할 때")
    void transfer_no_money_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(3333L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(0L);
        accountTransferReqDto.setTransactionType("TRANSFER");

        String requestBody = om.writeValueAsString(accountTransferReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/transfer")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("0원 이하의 금액을 이체할 수 없습니다."));
    }

    @WithUserDetails(value = "griotold", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("거래 타입에 이상한 값을 넣으려 할 때")
    void transfer_invalid_transactionType_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(3333L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(0L);
        accountTransferReqDto.setTransactionType("BLAHBLAH");

        String requestBody = om.writeValueAsString(accountTransferReqDto);
        log.debug("테스트 : requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/accounts/transfer")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        log.debug("테스트 : responseBody = {}", responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.msg").value("유효성 검사 실패"));

    }
}