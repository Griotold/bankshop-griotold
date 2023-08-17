package com.griotold.bankshop.config.dummy;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemImgRepository;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.transaction.TransactionRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.ztudy.Member;
import com.griotold.bankshop.ztudy.MemberJpaRepository;
import com.griotold.bankshop.ztudy.Team;
import com.griotold.bankshop.ztudy.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Configuration
public class DummyDevInit extends DummyObject{
    @Transactional // EntityManager 단독으로 쓰려고
    @Profile("dev")
    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           ItemRepository itemRepository,
                           AccountRepository accountRepository,
                           TransactionRepository transactionRepository,
                           MemberJpaRepository memberJpaRepository,
                           TeamRepository teamRepository,
                           ItemImgRepository itemImgRepository) {
        return (args) -> {
             User admin = userRepository.save(newAdminUser("admin", "관리자"));
            User griotold = userRepository.save(newUser("griotold", "고리오영감"));
            User kandela = userRepository.save(newUser("kandela", "칸델라"));
            User rien = userRepository.save(newUser("rien", "리앵"));

            Item snack4Cat = itemRepository.save(newItem("츄르"));
            Item sweeper = itemRepository.save(newItem("안경닦이"));
            Item tissue = itemRepository.save(newItem("물티슈"));

            itemImgRepository.save(newItemImg("츄르 이미지", snack4Cat));
            itemImgRepository.save(newItemImg("안경닦이 이미지", sweeper));
            itemImgRepository.save(newItemImg("물티슈 이미지", tissue));


            Account griotoldAccount1 = accountRepository.save(newAccount(1111L, griotold));
            Account kandelaAccount1 = accountRepository.save(newAccount(2222L, kandela));
            Account rienAccount1 = accountRepository.save(newAccount(3333L, rien));
            Account griotoldAccount2 = accountRepository.save(newAccount(4444L, griotold));

            transactionRepository.save(newWithdrawTransaction(griotoldAccount1, accountRepository));
            transactionRepository.save(newDepositTransaction(kandelaAccount1, accountRepository));
            transactionRepository.save(newTransferTransaction(griotoldAccount1, kandelaAccount1, accountRepository));
            transactionRepository.save(newTransferTransaction(griotoldAccount1, rienAccount1, accountRepository));
            transactionRepository.save(newTransferTransaction(kandelaAccount1, griotoldAccount1, accountRepository));

            /**
             * QueryDSL용 더미 데이터
             * */
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            teamRepository.save(teamA);
            teamRepository.save(teamB);

            for (int i = 0; i < 100; i++) {
                Team selectedTeam = i % 2 == 0? teamA : teamB;
                memberJpaRepository.save(new Member("member" + i, i, selectedTeam));
            }
        };
    }

}
