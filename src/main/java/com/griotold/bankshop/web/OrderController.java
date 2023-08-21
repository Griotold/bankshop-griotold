package com.griotold.bankshop.web;

import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.order.OrderReqDto;
import com.griotold.bankshop.dto.order.OrderRespDto;
import com.griotold.bankshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.griotold.bankshop.dto.order.OrderReqDto.*;
import static com.griotold.bankshop.dto.order.OrderRespDto.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/s/orders")
    public ResponseEntity<?> order(@RequestBody @Valid OrderDto orderDto,
                                   BindingResult bindingResult,
                                   @AuthenticationPrincipal LoginUser loginUser) {

        OrderHistDto orderHistDto = orderService.order(orderDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 완료", orderHistDto),
                HttpStatus.CREATED);
    }
}
