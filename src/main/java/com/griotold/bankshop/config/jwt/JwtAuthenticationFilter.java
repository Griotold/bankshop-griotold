package com.griotold.bankshop.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.dto.user.UserReqDto;
import com.griotold.bankshop.dto.user.UserRespDto;
import com.griotold.bankshop.utils.CustomResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;

import static com.griotold.bankshop.dto.user.UserReqDto.*;
import static com.griotold.bankshop.dto.user.UserRespDto.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.debug("디버그 : attemptAuthentication 호출됨");
        try {
            ObjectMapper om = new ObjectMapper();
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginReqDto.getUsername(), loginReqDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        log.debug("디버그 : 로그인 성공 - successfulAuthentication 호출됨");
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        String jwtToken = JwtProcess.create(loginUser);
        response.addHeader(JwtVO.HEADER, jwtToken);

        LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser());
        CustomResponseUtil.success(response, loginRespDto, HttpStatus.OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException failed) throws IOException, ServletException {
        log.debug("디버그 : 로그인 실패 - unsuccessfulAuthentication 호출됨");
        CustomResponseUtil.fail(response, "로그인 실패", HttpStatus.BAD_REQUEST);
    }
}
