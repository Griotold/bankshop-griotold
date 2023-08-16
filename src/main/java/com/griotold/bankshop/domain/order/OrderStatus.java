package com.griotold.bankshop.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER("주문"), CANCEL("취소") ;

    private String value;
}
