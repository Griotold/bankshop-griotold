package com.griotold.bankshop.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.griotold.bankshop.config.auth.LoginUser;
import com.griotold.bankshop.handler.ex.CustomJwtException;
import com.griotold.bankshop.utils.CustomResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            if (isHeaderVerify(request)) {
                log.debug("디버그 : 토큰이 존재함");
                String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
                LoginUser loginUser = JwtProcess.verify(token);
                log.debug("디버그 : 토큰 검증 완료");

                if (loginUser != null) {
                    Authentication authentication
                            = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("디버그 : 임시 세션 생성");
                }

            }
            chain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            CustomResponseUtil.fail(response, "로그인을 다시 해주세요", HttpStatus.UNAUTHORIZED);
        }

    }
    private boolean isHeaderVerify (HttpServletRequest request){
        String header = request.getHeader(JwtVO.HEADER);
        if (header == null || !header.startsWith(JwtVO.TOKEN_PREFIX)) {
            return false;
        } else {
            return true;
        }
    }
}
