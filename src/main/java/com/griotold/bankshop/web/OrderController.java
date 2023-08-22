package com.griotold.bankshop.web;

import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.account.AccountRespDto;
import com.griotold.bankshop.dto.order.OrderReqDto;
import com.griotold.bankshop.dto.order.OrderRespDto;
import com.griotold.bankshop.service.OrderService;
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

        OrderReturnDto orderReturnDto = orderService.order(orderDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 완료", orderReturnDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/s/orders/login-user")
    public ResponseEntity<?> retrieveOrderList(@RequestParam(value = "status", defaultValue = "ALL") String orderStatus,
                                               @PageableDefault(size = 5) Pageable pageable,
                                                @AuthenticationPrincipal LoginUser loginUser) {
        OrderHistDto orderHistDto = orderService.historyList(loginUser.getUser().getId(), orderStatus, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 이력 조회", orderHistDto),
                HttpStatus.OK);
    }

}
