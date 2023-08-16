package com.griotold.bankshop.domain.transaction;

import com.griotold.bankshop.config.QueryDSLConfig;
import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@ActiveProfiles("test")
@DataJpaTest
@Import(QueryDSLConfig.class)
class TransactionRepositoryImplTest extends DummyObject {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void setUp() {
        autoIncrementRest();
        dataSetting();
        em.clear();
    }
    @Test
    public void dataJpa_test() {
        List<Transaction> all = transactionRepository.findAll();
        for (Transaction transaction : all) {
            log.debug("테스트 : transaction.getId = {}", transaction.getId());
            log.debug("테스트 : transaction.getTransactionType = {}", transaction.getTransactionType().getValue());
            log.debug("테스트 : transaction.getSender = {}", transaction.getSender());
            log.debug("테스트 : transaction.getReceiver = {}", transaction.getReceiver());
            log.debug("=================================");
        }
    }
    @Test
    public void dataJpa_AutoIncrement_test() {
        List<Transaction> all = transactionRepository.findAll();
        for (Transaction transaction : all) {
            log.debug("테스트 : transaction.getId = {}", transaction.getId());
            log.debug("테스트 : transaction.getTransactionType = {}", transaction.getTransactionType().getValue());
            log.debug("테스트 : transaction.getSender = {}", transaction.getSender());
            log.debug("테스트 : transaction.getReceiver = {}", transaction.getReceiver());
            log.debug("=================================");
        }
    }

    @Test
    @DisplayName("transactionType - ALL")
    void transactionType_ALL_test() throws Exception {
        // given
        Long accountId = 1L;
        String transactionType = "ALL";

        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<Transaction> all = transactionRepository.findTransactionList(accountId, transactionType, pageRequest);
        all.stream().forEach((t) -> {
                log.debug("테스트 : t.getId = {}", t.getId());
                log.debug("테스트 : t.getAmount = {}", t.getAmount());
                log.debug("테스트 : t.getSender = {}", t.getSender());
                log.debug("테스트 : t.getReceiver = {}", t.getReceiver());
                log.debug("테스트 : t.getDepositAccountBalance = {}", t.getDepositAccountBalance());
                log.debug("테스트 : t.getWithdrawAccountBalance = {}", t.getWithdrawAccountBalance());
                log.debug("======================================");
        });
        // then
        assertThat(all.getSize()).isEqualTo(4);
    }
    @Test
    @DisplayName("transactionType - WITHDRAW")
    void transactionType_WITHDRAW_test() throws Exception {
        // given
        Long accountId = 1L;
        String transactionType = "WITHDRAW";

        PageRequest pageRequest = PageRequest.of(0, 5);


        // when
        Page<Transaction> all = transactionRepository.findTransactionList(accountId, transactionType, pageRequest);
        all.stream().forEach((t) -> {
            log.debug("테스트 : t.getId = {}", t.getId());
            log.debug("테스트 : t.getAmount = {}", t.getAmount());
            log.debug("테스트 : t.getSender = {}", t.getSender());
            log.debug("테스트 : t.getReceiver = {}", t.getReceiver());
            log.debug("테스트 : t.getDepositAccountBalance = {}", t.getDepositAccountBalance());
            log.debug("테스트 : t.getWithdrawAccountBalance = {}", t.getWithdrawAccountBalance());
            log.debug("======================================");
        });
        // then
        assertThat(all.getSize()).isEqualTo(3);
    }
    @Test
    @DisplayName("transactionType - DEPOSIT")
    void transactionType_DEPOSIT_test() throws Exception {
        // given
        Long accountId = 1L;
        String transactionType = "DEPOSIT";

        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Page<Transaction> all = transactionRepository.findTransactionList(accountId, transactionType, pageRequest);
        all.stream().forEach((t) -> {
            log.debug("테스트 : t.getId = {}", t.getId());
            log.debug("테스트 : t.getAmount = {}", t.getAmount());
            log.debug("테스트 : t.getSender = {}", t.getSender());
            log.debug("테스트 : t.getReceiver = {}", t.getReceiver());
            log.debug("테스트 : t.getDepositAccountBalance = {}", t.getDepositAccountBalance());
            log.debug("테스트 : t.getWithdrawAccountBalance = {}", t.getWithdrawAccountBalance());
            log.debug("======================================");
        });

        // then
        assertThat(all.getSize()).isEqualTo(1);
    }


    private void dataSetting() {
        User griotold = userRepository.save(newUser("griotold", "고리오영감"));
        User kandela = userRepository.save(newUser("kandela", "칸델라"));
        User rien = userRepository.save(newUser("rien", "리앵"));
        User admin = userRepository.save(newUser("admin", "관리자"));

        Account griotoldAccount1 = accountRepository.save(newAccount(1111L, griotold));
        Account kandelaAccount1 = accountRepository.save(newAccount(2222L, kandela));
        Account rienAccount1 = accountRepository.save(newAccount(3333L, rien));
        Account griotoldAccount2 = accountRepository.save(newAccount(4444L, griotold));

        transactionRepository.save(newWithdrawTransaction(griotoldAccount1, accountRepository));
        transactionRepository.save(newDepositTransaction(kandelaAccount1, accountRepository));
        transactionRepository.save(newTransferTransaction(griotoldAccount1, kandelaAccount1, accountRepository));
        transactionRepository.save(newTransferTransaction(griotoldAccount1, rienAccount1, accountRepository));
        transactionRepository.save(newTransferTransaction(kandelaAccount1, griotoldAccount1, accountRepository));
    }

    private void autoIncrementRest() {
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN user_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN account_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE item_tb ALTER COLUMN item_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN transaction_id RESTART WITH 1").executeUpdate();
    }
}