package com.griotold.bankshop.dto.user;

import com.griotold.bankshop.user.User;
import com.griotold.bankshop.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;

public class UserReqDto {

    @Getter
    @Setter
    public static class JoinReqDto {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String email;
        @NotEmpty
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
