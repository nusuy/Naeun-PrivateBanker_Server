package com.naeun.naeun_server.global.common.filter;

import com.naeun.naeun_server.domain.User.domain.User;
import com.naeun.naeun_server.global.error.ErrorCode;
import com.naeun.naeun_server.global.error.GlobalErrorCode;
import com.naeun.naeun_server.global.error.exception.AppException;
import com.naeun.naeun_server.global.util.JwtUtil;
import com.naeun.naeun_server.global.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter implements Filter {
    private final JwtUtil jwtUtil;
    final String LOGIN_PATH = "/api/v1/user";
    final String TOKEN_PATH = "/api/v1/user/token";

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("Filter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();

        // Pass filter when login requested.
        if (requestURI.equals(LOGIN_PATH) && req.getMethod().equals("POST")) {
            chain.doFilter(request, response);
            return;
        } else if (requestURI.equals(TOKEN_PATH) && req.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        }

        // Validate JWT
        try {
            String header = req.getHeader("Authorization");
            if (header == null)
                throw new AppException(GlobalErrorCode.ACCESS_TOKEN_REQUIRED);

            User user = jwtUtil.validateToken(true, header);
            req.setAttribute("user", user);

            chain.doFilter(request, response);
        } catch (AppException e) {
            ResponseUtil.setResponse(res, e.getErrorCode().getHttpStatus().value(), e.getMessage());
        } catch (JwtException e) {
            ErrorCode code = GlobalErrorCode.INVALID_TOKEN;
            if (e instanceof ExpiredJwtException)
                code = GlobalErrorCode.EXPIRED_JWT;

            ResponseUtil.setResponse(res, code.getHttpStatus().value(), code.getMessage());
        }
    }

    @Override
    public void destroy() {
        log.info("Filter destroyed.");
    }
}
