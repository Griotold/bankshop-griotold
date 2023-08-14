package com.griotold.bankshop.dto.transaction;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.transaction.Transaction;
import com.griotold.bankshop.utils.CustomDateUtil;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TransactionRespDto {


    @Getter
    @Setter
    public static class AccountDetailRespDto {
        private Long accountId;
        private Long number;
        private Long balance;
        private List<TransactionDto> transactionDtos = new ArrayList<>();

        public AccountDetailRespDto(Account account, List<Transaction> transactions) {
            this.accountId = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transactionDtos = transactions.stream()
                    .map((t) -> new TransactionDto(t, account.getNumber()))
                    .collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class TransactionDto {
            private Long id;
            private String transactionType;
            private Long amount;

            private String sender;
            private String receiver;

            private String tel;
            private String createdAt;
            private Long balance;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                this.id = transaction.getId();
                this.transactionType = transaction.getTransactionType().getValue();
                this.amount = transaction.getAmount();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
                this.tel = transaction.getTel() == null ? "없음" : transaction.getTel();

                if (transaction.getDepositAccount() == null) {
                    this.balance = transaction.getWithdrawAccountBalance();
                } else if (transaction.getWithdrawAccount() == null) {
                    this.balance = transaction.getDepositAccountBalance();
                } else {
                    if (accountNumber.longValue() == transaction.getDepositAccount().getNumber().longValue()) {
                        this.balance = transaction.getDepositAccountBalance();
                    } else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }
            }
        }
    }

    @Getter
    @Setter
    public static class TransactionListRespDto {

        private List<TransactionDto> transactionDtos = new ArrayList<>();

        public TransactionListRespDto(Account account, List<Transaction> transactions) {
            this.transactionDtos = transactions.stream()
                    .map((transaction) -> new TransactionDto(transaction, account.getNumber()))
                    .collect(Collectors.toList());
        }


        @Getter
        @Setter
        public static class TransactionDto {
            private Long id;
            private String transactionType;
            private Long amount;
            private String sender;
            private String receiver;
            private String tel;
            private String createdAt;
            private Long balance;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                this.id = transaction.getId();
                this.transactionType = transaction.getTransactionType().getValue();
                this.amount = transaction.getAmount();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
                this.tel = transaction.getTel() == null ? "없음" : transaction.getTel();

                if (transaction.getDepositAccount() == null) {
                    this.balance = transaction.getWithdrawAccountBalance();
                } else if (transaction.getWithdrawAccount() == null) {
                    this.balance = transaction.getDepositAccountBalance();
                } else {
                    if (accountNumber.longValue() == transaction.getDepositAccount().getNumber().longValue()) {
                        this.balance = transaction.getDepositAccountBalance();
                    } else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }
            }
        }
    }
}
