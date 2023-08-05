package com.griotold.bankshop.dto.user;

import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserReqDto {
    @Getter
    @Setter
    public static class LoginReqDto {
        private String username;
        private String password;
    }


    @Getter
    @Setter
    public static class JoinReqDto {
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문, 숫자 2~20자 이내로 작성해주세요!")
        @NotEmpty
        private String username;
        @Size(min = 4, max = 20, message = "4 ~ 20자 이내로 작성해주세요!")
        @NotEmpty
        private String password;
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z0-9]{2,3}$",
                message = "이메일 형식으로 작성해주세요!")
        @NotEmpty
        private String email;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글, 영문 1~20자 이내로 작성해주세요!")
        private String fullName;
        @NotEmpty
        private String address;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullName(fullName)
                    .address(address)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }
}
