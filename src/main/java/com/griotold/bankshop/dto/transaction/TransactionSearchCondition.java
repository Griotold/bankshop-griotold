package com.griotold.bankshop.dto.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TransactionSearchCondition {

    private String transactionType;
    private Integer page;

    public TransactionSearchCondition() {
        this.transactionType = "ALL";
        this.page = 0;
    }
}
