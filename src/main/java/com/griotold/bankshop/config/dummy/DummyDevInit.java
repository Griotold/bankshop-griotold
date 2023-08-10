package com.griotold.bankshop.config.dummy;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.transaction.TransactionRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit extends DummyObject{

    @Profile("dev")
    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           ItemRepository itemRepository,
                           AccountRepository accountRepository,
                           TransactionRepository transactionRepository) {
        return (args) -> {
             User admin = userRepository.save(newAdminUser("admin", "관리자"));
            User griotold = userRepository.save(newUser("griotold", "고리오영감"));
            User kandela = userRepository.save(newUser("kandela", "칸델라"));
            User rien = userRepository.save(newUser("rien", "리앵"));

            itemRepository.save(newItem("츄르"));
            itemRepository.save(newItem("안경닦이"));
            itemRepository.save(newItem("물티슈"));

            Account griotoldAccount1 = accountRepository.save(newAccount(1111L, griotold));
            Account kandelaAccount1 = accountRepository.save(newAccount(2222L, kandela));
            Account rienAccount1 = accountRepository.save(newAccount(3333L, rien));
            Account griotoldAccount2 = accountRepository.save(newAccount(4444L, griotold));

            transactionRepository.save(newWithdrawTransaction(griotoldAccount1, accountRepository));
            transactionRepository.save(newDepositTransaction(kandelaAccount1, accountRepository));
            transactionRepository.save(newTransferTransaction(griotoldAccount1, kandelaAccount1, accountRepository));
            transactionRepository.save(newTransferTransaction(griotoldAccount1, rienAccount1, accountRepository));
            transactionRepository.save(newTransferTransaction(kandelaAccount1, griotoldAccount1, accountRepository));
        };
    }

}
