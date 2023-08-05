package com.griotold.bankshop.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JwtProcessTest {

    @Test
    @DisplayName("토큰 생성 테스트")
    void create_test() {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);

        // when
        String jwtToken = JwtProcess.create(loginUser);

        // then
        assertThat(jwtToken).startsWith(JwtVO.TOKEN_PREFIX);
    }

    @Test
    @DisplayName("토큰 검증")
    void verify_test() {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser1 = new LoginUser(user);

        // when
        String jwtToken = JwtProcess.create(loginUser1);
        String replaced = jwtToken.replace(JwtVO.TOKEN_PREFIX, "");

        LoginUser loginUser2 = JwtProcess.verify(replaced);
        Long id = loginUser2.getUser().getId();
        String role = loginUser2.getUser().getRole().name();

        // then
        assertThat(id).isEqualTo(1L);
        assertThat(role).isEqualTo(UserEnum.CUSTOMER.name());
    }

    @Test
    @DisplayName("토큰 검증 실패")
    void verify_fail_test() {
        // given
        String invalidToken = "InvalidTokenString"; // 유효하지 않은 토큰 문자열

        // then
        assertThatThrownBy(() -> JwtProcess.verify(invalidToken))
                .isInstanceOf(JWTVerificationException.class);
    }
}