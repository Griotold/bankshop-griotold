package com.griotold.bankshop.web;

import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.account.AccountReqDto;
import com.griotold.bankshop.dto.account.AccountRespDto;
import com.griotold.bankshop.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.griotold.bankshop.dto.account.AccountReqDto.*;
import static com.griotold.bankshop.dto.account.AccountRespDto.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/s/accounts")
    public ResponseEntity<?> registerAccount(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto,
                                             BindingResult bindingResult,
                                             @AuthenticationPrincipal LoginUser loginUser) {
        AccountSaveRespDto accountSaveRespDto
                = accountService.register(accountSaveReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 등록 성공", accountSaveRespDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/s/accounts/login-user")
    public ResponseEntity<?> retrieveUserAccountList(@AuthenticationPrincipal LoginUser loginUser) {
        AccountListRespDto accountListRespDto = accountService.accountList(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 목록 보기_유저별 성공", accountListRespDto),
                HttpStatus.OK);
    }
}
