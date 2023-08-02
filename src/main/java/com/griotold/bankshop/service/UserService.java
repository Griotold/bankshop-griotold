package com.griotold.bankshop.service;

import com.griotold.bankshop.dto.user.UserReqDto;
import com.griotold.bankshop.dto.user.UserRespDto;
import com.griotold.bankshop.handler.ex.CustomApiException;
import com.griotold.bankshop.user.User;
import com.griotold.bankshop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.griotold.bankshop.dto.user.UserReqDto.*;
import static com.griotold.bankshop.dto.user.UserRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JoinRespDto join(JoinReqDto joinReqDto) {
        Optional<User> userOP = userRepository.findByUsername(joinReqDto.getUsername());
        if (userOP.isPresent()) {
            throw new CustomApiException("동일한 username이 존재합니다.");
        }

        User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder));

        return new JoinRespDto(userPS);

    }
}
