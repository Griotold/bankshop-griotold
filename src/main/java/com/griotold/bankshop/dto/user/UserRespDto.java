package com.griotold.bankshop.dto.user;

import com.griotold.bankshop.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserRespDto {
    @ToString
    @Getter
    @Setter
    public static class JoinRespDto {
        private Long id;
        private String username;
        private String fullName;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
        }
    }
}
