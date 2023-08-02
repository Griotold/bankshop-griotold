package com.griotold.bankshop.web;

import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.user.UserReqDto;
import com.griotold.bankshop.dto.user.UserRespDto;
import com.griotold.bankshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import static com.griotold.bankshop.dto.user.UserReqDto.*;
import static com.griotold.bankshop.dto.user.UserRespDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto,
                                  BindingResult bindingResult) {

        JoinRespDto joinRespDto = userService.join(joinReqDto);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "회원가입 성공", joinRespDto),
                HttpStatus.CREATED);
    }
}
