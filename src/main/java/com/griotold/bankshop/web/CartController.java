package com.griotold.bankshop.web;

import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.cart.CartReqDto;
import com.griotold.bankshop.dto.cart.CartRespDto;
import com.griotold.bankshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static com.griotold.bankshop.dto.cart.CartReqDto.*;
import static com.griotold.bankshop.dto.cart.CartRespDto.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CartController {

    private final CartService cartService;

    @PostMapping("/s/cart/items")
    public ResponseEntity<?> addCart(@RequestBody @Valid CartItemDto cartItemDto,
                                     BindingResult bindingResult,
                                     @AuthenticationPrincipal LoginUser loginUser) {
        CartAddRespDto cartAddRespDto = cartService.addCart(cartItemDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 담기 성공", cartAddRespDto),
                HttpStatus.CREATED);
    }
    @GetMapping("/s/cart/items")
    public ResponseEntity<?> retrieveCartItems(@PageableDefault(size = 5)Pageable pageable,
                                               @AuthenticationPrincipal LoginUser loginUser) {
        CartDetailRespDto cartDetailRespDto = cartService.retrieveCartItem(loginUser.getUser().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 목록 보기", cartDetailRespDto),
                HttpStatus.OK);
    }

    @PutMapping("/s/cart/items/{cartItemId}")
    public ResponseEntity<?> updateCartItemCount(@PathVariable Long cartItemId,
                                                 @RequestBody @Valid CartItemUpdateReqDto cartItemUpdateReqDto,
                                                 BindingResult bindingResult,
                                                 @AuthenticationPrincipal LoginUser loginUser) {
        CartItemUpdateRespDto cartItemUpdateRespDto
                = cartService.updateCartItemCount(cartItemUpdateReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 상품 수량 업데이트 성공", cartItemUpdateRespDto),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/s/cart/items/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId,
                                            @AuthenticationPrincipal LoginUser loginUser) {
        cartService.deleteCartItem(cartItemId, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 상품 삭제 성공", null),
                HttpStatus.OK);
    }

    @PostMapping("/s/cart/orders")
    public ResponseEntity<?> orderCart(@RequestBody @Valid CartOrderDto cartOrderDto,
                                       BindingResult bindingResult,
                                       @AuthenticationPrincipal LoginUser loginUser) {
        CartOrderRespDto cartOrderRespDto = cartService.orderCartItem(cartOrderDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 주문 성공", cartOrderRespDto),
                HttpStatus.CREATED);
    }

}
