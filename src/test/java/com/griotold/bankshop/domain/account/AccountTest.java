package com.griotold.bankshop.domain.account;

import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.handler.ex.CustomApiException;
import com.griotold.bankshop.handler.ex.CustomForbiddenException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest extends DummyObject {

    private Account account;

    @BeforeEach
    void dataSet() {
        User griotold = newMockUser(1L, "griotold", "고리오영감");
        account = newMockAccount(1L, 1111L, 1000L, griotold);
    }

    @Test
    @DisplayName("계좌 소유자 검증 테스트")
    void checkOwner_test() throws Exception{
        // given
        Long userId = 2L;

        // when + then
        assertThatThrownBy(
                () -> account.checkOwner(userId)).isInstanceOf(CustomForbiddenException.class);
    }

    @Test
    @DisplayName("계좌 비밀번호 검증 테스트")
    void checkSamePassword_test() throws Exception {
        // given
        Long password = 9999L;

        // when + then
        assertThatThrownBy(
                () -> account.checkSamePassword(password)).isInstanceOf(CustomApiException.class);
    }

    @Test
    @DisplayName("계좌 잔액 검증 테스트")
    void checkBalance_test() throws Exception{
        // given
        Long amount = 1500L;

        // when + then
        assertThatThrownBy(() -> account.checkBalance(amount))
                .isInstanceOf(CustomApiException.class);
    }

    @Test
    @DisplayName("입금 테스트")
    void deposit_test() throws Exception {
        // given
        Long amount = 5000L;

        // when
        account.deposit(amount);

        // then
        assertThat(account.getBalance()).isEqualTo(6000L);
    }

    @Test
    @DisplayName("출금 테스트")
    void withdraw_test() throws Exception {
        // given
        Long amount = 500L;

        // when
        account.withdraw(amount);

        // then
        assertThat(account.getBalance()).isEqualTo(500L);
    }

}