package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.user.User;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.griotold.bankshop.dto.account.AccountReqDto.*;
import static com.griotold.bankshop.dto.account.AccountRespDto.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public AccountSaveRespDto register(AccountSaveReqDto accountSaveReqDto,
                                       Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을 수 없습니다."));

        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if (accountOP.isPresent()) {
            throw new CustomApiException("해당 계좌가 이미 존재합니다.");
        }

        Account account = accountSaveReqDto.toEntity(userPS);
        Account accountPS = accountRepository.save(account);

        return new AccountSaveRespDto(accountPS);
    }

    public AccountListRespDto accountList(Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(()
                -> new CustomApiException("유저를 찾을 수 없습니다."));
        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        return new AccountListRespDto(userPS, accountListPS);
    }

    public AccountListAdminRespDto accountListAdmin() {
        List<Account> all = accountRepository.findAll();
        return new AccountListAdminRespDto(all);
    }

    @Transactional
    public void deleteAccount(Long number, Long userId) {
        Account accountPS = accountRepository.findByNumber(number)
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        accountPS.checkOwner(userId);

        accountRepository.deleteById(accountPS.getId());

    }


}
