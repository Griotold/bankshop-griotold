package com.griotold.bankshop.config.dummy;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemSellStatus;
import com.griotold.bankshop.domain.transaction.Transaction;
import com.griotold.bankshop.domain.transaction.TransactionType;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    protected User newAdminUser(String username, String fullName) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("1234");

        return User.builder()
                .username(username)
                .password(encodedPassword)
                .email(username + "@nate.com")
                .fullName(fullName)
                .address("행복시 희망구 사랑동 123번지")
                .role(UserEnum.ADMIN)
                .build();
    }
    protected User newUser(String username, String fullName) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("1234");

        return User.builder()
                .username(username)
                .password(encodedPassword)
                .email(username + "@nate.com")
                .fullName(fullName)
                .address("행복시 희망구 사랑동 123번지")
                .role(UserEnum.CUSTOMER)
                .build();
    }

    protected User newMockUser(Long id, String username, String fullName) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("1234");

        return User.builder()
                .id(id)
                .username(username)
                .password(encodedPassword)
                .email(username + "@nate.com")
                .fullName(fullName)
                .address("행복시 희망구 사랑동 123번지")
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Item newItem(String itemName) {

        return Item.builder()
                .itemName(itemName)
                .price(10000)
                .stockNumber(100)
                .itemDetail("테스트 아이템 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();
    }

    protected Account newAccount(Long number, User user) {
        return Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .user(user)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, Long balance, User user) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository){
        account.withdraw(100L);

        if (accountRepository != null) {
            accountRepository.save(account);
        }
        Transaction transaction = Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(100L)
                .transactionType(TransactionType.WITHDRAW)
                .sender(account.getNumber().toString())
                .receiver("ATM")
                .build();
        return transaction;
    }
    protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository){
        account.deposit(100L);
        if (accountRepository != null) {
            accountRepository.save(account);
        }
        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(100L)
                .transactionType(TransactionType.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber().toString())
                .tel("01022227777")
                .build();
        return transaction;
    }

    protected Transaction newTransferTransaction(Account withdrawAccount,
                                                 Account depositAccount,
                                                 AccountRepository accountRepository){
        withdrawAccount.withdraw(100L);
        depositAccount.deposit(100L);

        if (accountRepository != null) {
            accountRepository.save(withdrawAccount);
            accountRepository.save(depositAccount);
        }
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(100L)
                .transactionType(TransactionType.TRANSFER)
                .sender(withdrawAccount.getNumber().toString())
                .receiver(depositAccount.getNumber().toString())
                .build();
        return transaction;

    }
}
