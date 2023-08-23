package com.griotold.bankshop.domain.transaction;

import com.griotold.bankshop.domain.account.Account;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction_tb")
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Account withdrawAccount;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Account depositAccount;

    @Column(nullable = false)
    private Long amount;

    private Long withdrawAccountBalance;
    private Long depositAccountBalance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private String sender;
    private String receiver;
    private String tel;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Transaction(Long id, Account withdrawAccount, Account depositAccount,
                       Long amount, Long withdrawAccountBalance, Long depositAccountBalance,
                       TransactionType transactionType, String sender, String receiver,
                       String tel, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.transactionType = transactionType;
        this.sender = sender;
        this.receiver = receiver;
        this.tel = tel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public static Transaction createDepositTransaction(Account account, Long amount, String tel) {
        return Transaction.builder()
                .depositAccount(account)
                .withdrawAccount(null)
                .depositAccountBalance(account.getBalance())
                .withdrawAccountBalance(null)
                .amount(amount)
                .transactionType(TransactionType.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber().toString())
                .tel(tel)
                .build();
    }
    public static Transaction createdWithdrawTransaction(Account account, Long amount){
        return Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(amount)
                .transactionType(TransactionType.WITHDRAW)
                .sender(account.getNumber().toString())
                .receiver("ATM")
                .build();
    }
    public static Transaction createTransferTransaction(Account withdrawAccount, Account depositAccount,
                                                        Long amount) {
        return Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(amount)
                .transactionType(TransactionType.TRANSFER)
                .sender(withdrawAccount.getNumber().toString())
                .receiver(depositAccount.getNumber().toString())
                .build();
    }
    public static Transaction createOrderTransaction(Account account, Long amount) {
        return Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(amount)
                .transactionType(TransactionType.TRANSFER)
                .sender(account.getNumber().toString())
                .receiver("BANKSHOP")
                .build();
    }
    public static Transaction createdCancelTransaction(Account account, Long returnAmount) {
        return Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(returnAmount)
                .transactionType(TransactionType.TRANSFER)
                .sender("BANKSHOP")
                .receiver(account.getNumber().toString())
                .build();
    }

}
