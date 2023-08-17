package com.griotold.bankshop.dto.item;

import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemSellStatus;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

public class ItemReqDto {


    @Getter
    @Setter
    public static class ItemRegisterReqDto {

        @NotEmpty(message = "상품명은 필수 입력 값입니다.")
        private String itemName;

        @NotNull(message = "가격은 필수 입력 값입니다.")
        @Digits(integer = 6, fraction = 4, message = "100만 이하 숫자를 입력하세요.")
        private Integer price;

        @NotNull(message = "재고는 필수 입력 값입니다.")
        @Digits(integer = 4, fraction = 4, message = "천 단위까지 입력 가능합니다.")
        private Integer stockNumber;

        @NotEmpty(message = "상품 상세 설명은 필수 입력 값입니다.")
        private String itemDetail;

        @NotEmpty(message = "이미지 이름은 필수 입력 값입니다.")
        private String imgName;

        @NotEmpty(message = "원본 이미지명은 필수 입력 값입니다.")
        private String oriImgName;

        @NotEmpty(message = "이미지 URL은 필수 입력 값입니다.")
        private String imgUrl;

        public Item toEntity() {
            return Item.builder()
                    .itemName(itemName)
                    .price(price)
                    .stockNumber(stockNumber)
                    .itemDetail(itemDetail)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();
        }

    }
}
