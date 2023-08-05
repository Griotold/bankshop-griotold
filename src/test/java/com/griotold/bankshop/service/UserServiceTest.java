package com.griotold.bankshop.service;

import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.griotold.bankshop.dto.user.UserReqDto.*;
import static com.griotold.bankshop.dto.user.UserRespDto.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 테스트")
    void join_test() {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("user");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("user@nate.com");
        joinReqDto.setFullName("고리오영감");
        joinReqDto.setAddress("행복시 희망구 사랑동 123번지");

        // stub 1
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        // stub 2
        User user = newMockUser(1L, "griotold", "고리오영감");
        when(userRepository.save(any())).thenReturn(user);

        // when
        JoinRespDto joinRespDto = userService.join(joinReqDto);

        // then
        assertThat(joinRespDto.getId()).isEqualTo(1L);
        assertThat(joinRespDto.getUsername()).isEqualTo("griotold");
        assertThat(joinRespDto.getFullName()).isEqualTo("고리오영감");
    }

}