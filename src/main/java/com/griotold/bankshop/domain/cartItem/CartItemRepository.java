package com.griotold.bankshop.domain.cartItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
}
