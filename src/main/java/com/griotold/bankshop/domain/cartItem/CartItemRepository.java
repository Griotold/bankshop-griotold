package com.griotold.bankshop.domain.cartItem;

import com.griotold.bankshop.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    List<CartItem> findByCart(Cart cart);
}
