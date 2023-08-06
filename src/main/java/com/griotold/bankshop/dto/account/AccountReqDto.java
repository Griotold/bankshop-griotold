package com.griotold.bankshop.dto.account;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

public class AccountReqDto {

    @Getter
    @Setter
    public static class AccountSaveReqDto {
        @NotNull
        @Max(value = 9999, message = "4자리 숫자를 입력해주세요.")
        @Min(value = 1000, message = "4자리 숫자를 입력해주세요.")
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        @Max(value = 9999, message = "4자리 숫자를 입력해주세요.")
        @Min(value = 1000, message = "4자리 숫자를 입력해주세요.")
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(User user) {
            return Account.builder().number(number)
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();
        }

    }
}
