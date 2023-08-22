package com.griotold.bankshop.dto.cart;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartReqDto {

    @Getter
    @Setter
    public static class CartItemDto {
        @NotNull(message = "상품 id는 필수 입력 값 입니다.")
        private Long itemId;

        @Min(value = 1, message = "최소 1개 이상 담아주세요.")
        private Integer count;
    }

    @Getter
    @Setter
    public static class CartItemUpdateReqDto {
        @NotNull(message = "장바구니 상품 id는 필수 입력값 입니다.")
        private Long cartItemId;

        @Min(value = 1, message = "최소 1개 이상 담아주세요.")
        private Integer count;

    }
}
