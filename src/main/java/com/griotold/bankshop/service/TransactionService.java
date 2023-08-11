package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.account.Account;
import com.griotold.bankshop.domain.account.AccountRepository;
import com.griotold.bankshop.domain.transaction.Transaction;
import com.griotold.bankshop.domain.transaction.TransactionRepository;
import com.griotold.bankshop.dto.transaction.TransactionRespDto;
import com.griotold.bankshop.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.griotold.bankshop.dto.transaction.TransactionRespDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionListRespDto transactionList(Long userId, Long accountNumber,
                                                  String transactionType, int page) {
        Account accountPS = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new CustomApiException("해당 계좌를 찾을 수 없습니다."));

        accountPS.checkOwner(userId);

        List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountPS.getId(), transactionType, page);
        return new TransactionListRespDto(accountPS, transactionListPS);
    }
}