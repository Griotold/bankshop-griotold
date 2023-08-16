package com.griotold.bankshop.web;

import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.account.AccountReqDto;
import com.griotold.bankshop.dto.account.AccountRespDto;
import com.griotold.bankshop.dto.transaction.TransactionRespDto;
import com.griotold.bankshop.service.AccountService;
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

import static com.griotold.bankshop.dto.account.AccountReqDto.*;
import static com.griotold.bankshop.dto.account.AccountRespDto.*;
import static com.griotold.bankshop.dto.transaction.TransactionRespDto.*;

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

    @GetMapping("/admin/accounts")
    public ResponseEntity<?> retrieveAllAccountList() {
        AccountListAdminRespDto accountListAdminRespDto = accountService.accountListAdmin();
        return new ResponseEntity<>(new ResponseDto<>(1, "전체 계좌 목록 보기_관리자 성공", accountListAdminRespDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/s/accounts/{number}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long number,
                                           @AuthenticationPrincipal LoginUser loginUser) {
        accountService.deleteAccount(number, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료", null),
                HttpStatus.OK);
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<?> deposit(@RequestBody @Valid AccountDepositReqDto accountDepositReqDto,
                                     BindingResult bindingResult) {
        AccountDepositRespDto accountDepositRespDto = accountService.deposit(accountDepositReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 입금 완료", accountDepositRespDto),
                HttpStatus.CREATED);
    }

    @PostMapping("/s/accounts/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody @Valid AccountWithdrawReqDto accountWithdrawReqDto,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal LoginUser loginUser) {
        AccountWithdrawRespDto accountWithdrawRespDto
                = accountService.withdraw(accountWithdrawReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 출금 완료", accountWithdrawRespDto),
                HttpStatus.CREATED);

    }

    @PostMapping("/s/accounts/transfer")
    public ResponseEntity<?> transfer(@RequestBody @Valid AccountTransferReqDto accountTransferReqDto,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal LoginUser loginUser) {
        AccountTransferRespDto accountTransferRespDto
                = accountService.transfer(accountTransferReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 이체 완료", accountTransferRespDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/s/accounts/{number}")
    public ResponseEntity<?> accountDetail(@PathVariable Long number,
                                           @PageableDefault(size = 5) Pageable pageable,
                                           @AuthenticationPrincipal LoginUser loginUser) {
        AccountDetailRespDto accountDetailRespDto
                = accountService.accountDetail(number, loginUser.getUser().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 상세보기 성공", accountDetailRespDto),
                HttpStatus.OK);
    }
}
