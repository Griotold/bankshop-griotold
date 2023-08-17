package com.griotold.bankshop.domain.cart;

import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CartTest extends DummyObject {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    void setUp() {
//        User admin = userRepository.save(newAdminUser("admin", "관리자"));
//        User griotold = userRepository.save(newUser("griotold", "고리오영감"));
//        User kandela = userRepository.save(newUser("kandela", "칸델라"));
//        User rien = userRepository.save(newUser("rien", "리앵"));
    }

    @Test
    @DisplayName("장바구니와 회원 매핑 테스트")
    void findCart_and_user() throws Exception {
        // given
        User griotold = userRepository.save(newUser("griotold", "고리오영감"));

        Cart cart = Cart.builder()
                .user(griotold)
                .build();

        // when
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart findedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(findedCart.getUser().getUsername())
                .isEqualTo("griotold");

    }

}