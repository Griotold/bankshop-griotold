package com.griotold.bankshop.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemSellStatus {

    SELL("판매중"), SOLD_OUT("품절"), ALL("모두");

    private String value;
}
