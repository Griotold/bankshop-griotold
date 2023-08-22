package com.griotold.bankshop.web;

import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.cart.CartReqDto;
import com.griotold.bankshop.dto.cart.CartRespDto;
import com.griotold.bankshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

}
