package com.griotold.bankshop.dto.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderReqDto {
    @Getter
    @Setter
    public static class OrderDto{
        @NotNull(message = "사용 계좌는 필수 입력 값입니다.")
        @Digits(integer = 4, fraction = 4)
        private Long accountNumber;

        @NotNull(message = "계좌 비밀번호를 입력하세요.")
        @Digits(integer = 4, fraction = 4)
        private Long accountPassword;

        @NotNull(message = "상품 id는 필수 입력 값입니다.")
        private Long itemId;

        @NotNull(message = "상품 id는 필수 입력 값입니다.")
        @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
        @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
        private Integer count;
    }
}
