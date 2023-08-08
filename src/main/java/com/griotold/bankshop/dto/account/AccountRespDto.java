package com.griotold.bankshop.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.transaction.Transaction;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.utils.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRespDto {

    @Getter
    @Setter
    public static class AccountTransferRespDto {
        private Long accountId;
        private Long number;
        private Long balance;
        private TransactionDto transactionDto;

        public AccountTransferRespDto(Account account, Transaction transaction) {
            this.accountId = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transactionDto = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public static class TransactionDto {
            private Long id;
            private String transactionType;
            private String sender;
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.transactionType = transaction.getTransactionType().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter
    @Setter
    public static class AccountWithdrawRespDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transactionDto;

        public AccountWithdrawRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transactionDto = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public static class TransactionDto {
            private Long id;
            private String transactionType;
            private String sender;
            private String receiver;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.transactionType = transaction.getTransactionType().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter
    @Setter
    public static class AccountDepositRespDto {
        private Long id;
        private Long number;
        private TransactionDto transactionDto;

        public AccountDepositRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transactionDto = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public static class TransactionDto {
            private Long id;
            private String transactionType;
            private String sender;
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;
            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.transactionType = transaction.getTransactionType().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

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
