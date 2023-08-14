package com.griotold.bankshop.web;

import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.domain.transaction.Transaction;
import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.transaction.TransactionRespDto;
import com.griotold.bankshop.dto.transaction.TransactionSearchCondition;
import com.griotold.bankshop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.griotold.bankshop.dto.transaction.TransactionRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/s/accounts/{number}/transactions")
    public ResponseEntity<?> transactionList(@PathVariable Long number,
                                             @RequestParam(value = "transactionType", defaultValue = "ALL") String transactionType,
                                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @AuthenticationPrincipal LoginUser loginUser) {
        TransactionListRespDto transactionListRespDto = transactionService.transactionList(loginUser.getUser().getId(), number, transactionType, page);
        return new ResponseEntity<>(new ResponseDto<>(1, "입출금 목록 보기 성공", transactionListRespDto),
                HttpStatus.OK);
    }


}
