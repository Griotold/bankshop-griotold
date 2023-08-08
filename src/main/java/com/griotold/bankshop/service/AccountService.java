package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.transaction.Transaction;
import com.griotold.bankshop.domain.transaction.TransactionRepository;
import com.griotold.bankshop.domain.transaction.TransactionType;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.griotold.bankshop.dto.account.AccountReqDto.*;
import static com.griotold.bankshop.dto.account.AccountRespDto.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountSaveRespDto register(AccountSaveReqDto accountSaveReqDto,
                                       Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을 수 없습니다."));

        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if (accountOP.isPresent()) {
            throw new CustomApiException("해당 계좌가 이미 존재합니다.");
        }

        Account account = accountSaveReqDto.toEntity(userPS);
        Account accountPS = accountRepository.save(account);

        return new AccountSaveRespDto(accountPS);
    }

    public AccountListRespDto accountList(Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(()
                -> new CustomApiException("유저를 찾을 수 없습니다."));
        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        return new AccountListRespDto(userPS, accountListPS);
    }

    public AccountListAdminRespDto accountListAdmin() {
        List<Account> all = accountRepository.findAll();
        return new AccountListAdminRespDto(all);
    }

    @Transactional
    public void deleteAccount(Long number, Long userId) {
        Account accountPS = accountRepository.findByNumber(number)
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        accountPS.checkOwner(userId);

        accountRepository.deleteById(accountPS.getId());

    }

    @Transactional
    public AccountDepositRespDto deposit(AccountDepositReqDto accountDepositReqDto) {
        if (accountDepositReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber())
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        depositAccountPS.deposit(accountDepositReqDto.getAmount());

        Transaction transaction = Transaction.builder()
                .depositAccount(depositAccountPS)
                .withdrawAccount(null)
                .depositAccountBalance(depositAccountPS.getBalance())
                .withdrawAccountBalance(null)
                .amount(accountDepositReqDto.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .sender("ATM")
                .receiver(accountDepositReqDto.getNumber().toString())
                .tel(accountDepositReqDto.getTel())
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);
        return new AccountDepositRespDto(depositAccountPS, transactionPS);
    }

    @Transactional
    public AccountWithdrawRespDto withdraw(AccountWithdrawReqDto accountWithdrawReqDto,
                                           Long userId) {
        if (accountWithdrawReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 출금할 수 없습니다.");
        }

        Account withdrawAccountPS = accountRepository.findByNumber(accountWithdrawReqDto.getNumber())
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        withdrawAccountPS.checkOwner(userId);

        withdrawAccountPS.checkSamePassword(accountWithdrawReqDto.getPassword());

        withdrawAccountPS.checkBalance(accountWithdrawReqDto.getAmount());

        withdrawAccountPS.withdraw(accountWithdrawReqDto.getAmount());

        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(null)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(null)
                .amount(accountWithdrawReqDto.getAmount())
                .transactionType(TransactionType.WITHDRAW)
                .sender(accountWithdrawReqDto.getNumber().toString())
                .receiver("ATM")
                .build();
        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountWithdrawRespDto(withdrawAccountPS, transactionPS);
    }

    @Transactional
    public AccountTransferRespDto transfer(AccountTransferReqDto accountTransferReqDto,
                                           Long userId) {
        if (accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber().longValue()) {
            throw new CustomApiException("입출금 계좌가 동일할 수 없습니다.");
        }

        if (accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 이체할 수 없습니다.");
        }

        Account withdrawAccountPS = accountRepository.findByNumber(accountTransferReqDto.getWithdrawNumber())
                .orElseThrow(() -> new CustomApiException("출금 계좌를 찾을 수 없습니다."));

        Account depositAccountPS = accountRepository.findByNumber(accountTransferReqDto.getDepositNumber())
                .orElseThrow(() -> new CustomApiException("입금 계좌를 찾을 수 없습니다."));

        withdrawAccountPS.checkOwner(userId);

        withdrawAccountPS.checkSamePassword(accountTransferReqDto.getWithdrawPassword());

        withdrawAccountPS.checkBalance(accountTransferReqDto.getAmount());

        withdrawAccountPS.withdraw(accountTransferReqDto.getAmount());
        depositAccountPS.deposit(accountTransferReqDto.getAmount());

        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(depositAccountPS)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(accountTransferReqDto.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .sender(accountTransferReqDto.getWithdrawNumber().toString())
                .receiver(accountTransferReqDto.getDepositNumber().toString())
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountTransferRespDto(withdrawAccountPS, transactionPS);
    }
}
