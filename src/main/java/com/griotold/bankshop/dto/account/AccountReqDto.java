package com.griotold.bankshop.dto.account;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import javax.validation.constraints.*;

public class AccountReqDto {

    @Getter
    @Setter
    public static class AccountTransferReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawNumber;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long depositNumber;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawPassword;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "TRANSFER")
        private String transactionType;
    }

    @Getter
    @Setter
    public static class AccountWithdrawReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "WITHDRAW")
        private String transactionType;
    }

    @Getter
    @Setter
    public static class AccountDepositReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "^(DEPOSIT)$")
        private String transactionType;

        @NotEmpty
        @Pattern(regexp = "^[0-9]{11}")
        private String tel;
    }

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
