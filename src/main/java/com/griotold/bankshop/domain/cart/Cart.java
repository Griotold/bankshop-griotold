package com.griotold.bankshop.domain.cart;

import com.griotold.bankshop.domain.user.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
@Table(name = "cart_tb")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Cart(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public static Cart createCart(User user){
        return Cart.builder()
                .user(user)
                .build();
    }
}
