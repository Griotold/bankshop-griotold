package com.griotold.bankshop.config.dummy;

import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemSellStatus;
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
}
