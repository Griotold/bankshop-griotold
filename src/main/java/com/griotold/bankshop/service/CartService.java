package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.cart.Cart;
import com.griotold.bankshop.domain.cart.CartRepository;
import com.griotold.bankshop.domain.cartItem.CartItem;
import com.griotold.bankshop.domain.cartItem.CartItemQueryRepository;
import com.griotold.bankshop.domain.cartItem.CartItemRepository;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.dto.cart.CartReqDto;
import com.griotold.bankshop.dto.order.OrderReqDto;
import com.griotold.bankshop.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.griotold.bankshop.dto.cart.CartReqDto.*;
import static com.griotold.bankshop.dto.cart.CartReqDto.CartItemDto;
import static com.griotold.bankshop.dto.cart.CartReqDto.CartItemUpdateReqDto;
import static com.griotold.bankshop.dto.cart.CartRespDto.*;
import static com.griotold.bankshop.dto.order.OrderReqDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemQueryRepository cartItemQueryRepository;
    private final OrderService orderService;

    @Transactional
    public CartAddRespDto addCart(CartItemDto cartItemDto, Long userId) {
        Item itemPS = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(() -> new CustomApiException("상품을 찾을 수 없습니다."));

        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Cart cartPS = cartRepository.findByUserId(userId);

        if (cartPS == null) {
            cartPS = Cart.createCart(userPS);
            cartRepository.save(cartPS);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cartPS.getId(), itemPS.getId());

        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return new CartAddRespDto(itemPS, savedCartItem);
        } else {
            CartItem cartItem
                    = CartItem.createCartItem(cartPS, itemPS, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return new CartAddRespDto(itemPS, cartItem);
        }
    }

    public CartDetailRespDto retrieveCartItem(Long userId, Pageable pageable) {

        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Page<CartItem> cartItemPG = cartItemQueryRepository.findCartItem(pageable);
        return new CartDetailRespDto(cartItemPG);
    }
    @Transactional
    public CartItemUpdateRespDto updateCartItemCount(CartItemUpdateReqDto cartItemUpdateReqDto,
                                                     Long userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        CartItem cartItemPS = cartItemRepository.findById(cartItemUpdateReqDto.getCartItemId())
                .orElseThrow(() -> new CustomApiException("장바구니 상품을 찾을 수 없습니다."));

        Cart cartPS = cartItemPS.getCart();

        cartPS.checkOwner(userId);

        cartItemPS.updateCount(cartItemUpdateReqDto.getCount());

        return new CartItemUpdateRespDto(cartItemPS);
    }

    @Transactional
    public void deleteCartItem(Long cartItemId, Long userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        CartItem cartItemPS = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomApiException("장바구니 상품을 찾을 수 없습니다."));

        Cart cartPS = cartItemPS.getCart();

        cartPS.checkOwner(userId);

        cartItemRepository.delete(cartItemPS);
    }

    @Transactional
    public CartOrderRespDto orderCartItem(CartOrderDto cartOrderDto, Long userId) {
        List<OrderCartDto> orderCartDtoList = new ArrayList<>();

        Cart cartPS = cartRepository.findByUserId(userId);
        List<CartItem> cartItemList = cartItemRepository.findByCart(cartPS);
        for (CartItem cartItem : cartItemList) {

            OrderCartDto orderCartDto = new OrderCartDto();
            orderCartDto.setItemId(cartItem.getItem().getId());
            orderCartDto.setCount(cartItem.getCount());
            orderCartDtoList.add(orderCartDto);
        }
        CartOrderRespDto cartOrderRespDto = orderService
                .orders(orderCartDtoList, userId, cartOrderDto.getAccountNumber(), cartOrderDto.getAccountPassword());

        for (CartItem cartItem : cartItemList) {
            cartItemRepository.delete(cartItem);
        }
        return cartOrderRespDto;
    }

}
