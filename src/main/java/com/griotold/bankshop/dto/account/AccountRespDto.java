package com.griotold.bankshop.dto.account;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRespDto {
    @Getter
    @Setter
    public static class AccountListAdminRespDto {
        private List<AccountDto> accountDtos = new ArrayList<>();

        public AccountListAdminRespDto(List<Account> accounts) {
            this.accountDtos = accounts.stream()
                    .map(AccountDto::new).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class AccountDto {
            private Long accountId;
            private Long userId;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.accountId = account.getId();
                this.userId = account.getUser().getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    @Getter
    @Setter
    public static class AccountListRespDto {
        private String fullName;
        private List<AccountDto> accountDtos = new ArrayList<>();

        public AccountListRespDto(User user, List<Account> accounts) {
            this.fullName = user.getFullName();
            this.accountDtos = accounts.stream()
                    .map(AccountDto::new).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    @Getter
    @Setter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }
}
