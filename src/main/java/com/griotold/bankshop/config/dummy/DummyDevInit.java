package com.griotold.bankshop.config.dummy;

import com.griotold.bankshop.domain.item.ItemRepository;
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
                           ItemRepository itemRepository) {
        return (args) -> {
             User admin = userRepository.save(newAdminUser("admin", "관리자"));
            User griotold = userRepository.save(newUser("griotold", "고리오영감"));

            itemRepository.save(newItem("츄르"));
            itemRepository.save(newItem("안경닦이"));
            itemRepository.save(newItem("물티슈"));
        };
    }

}
