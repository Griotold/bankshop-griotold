package com.griotold.bankshop.domain.account;

import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.handler.ex.CustomApiException;
import com.griotold.bankshop.handler.ex.CustomForbiddenException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_tb")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 4)
    private Long number;

    @Column(nullable = false, length = 4)
    private Long password;

    @Column(nullable = false)
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Builder
    public Account(Long id, Long number, Long password, Long balance, User user,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void checkOwner(Long userId) {
        if (user.getId().longValue() != userId.longValue()) {
            throw new CustomForbiddenException("계좌 소유자가 아닙니다.");
        }
    }

    public void deposit(Long amount) {
        balance += amount;
    }

    public void checkSamePassword(Long password) {
        if (this.password.longValue() != password.longValue()) {
            throw new CustomApiException("계좌 비밀번호 검증에 실패했습니다.");
        }
    }

    public void checkBalance(Long amount) {
        if (this.balance < amount) {
            throw new CustomApiException("계좌 잔액이 부족합니다.");
        }
    }

    public void withdraw(Long amount) {
        checkBalance(amount);
        this.balance -= amount;
    }
}
