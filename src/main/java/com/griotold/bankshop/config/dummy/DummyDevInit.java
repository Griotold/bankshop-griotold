package com.griotold.bankshop.config.dummy;

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
    CommandLineRunner init(UserRepository userRepository) {
        return (args) -> {
             User admin = userRepository.save(newAdminUser("admin", "관리자"));
            User griotold = userRepository.save(newUser("griotold", "고리오영감"));
        };
    }

}
