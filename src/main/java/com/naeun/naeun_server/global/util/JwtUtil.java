package com.naeun.naeun_server.global.util;

import com.naeun.naeun_server.domain.User.domain.User;
import com.naeun.naeun_server.domain.User.domain.UserRepository;
import com.naeun.naeun_server.domain.model.JwtVo;
import com.naeun.naeun_server.global.error.GlobalErrorCode;
import com.naeun.naeun_server.global.error.exception.AppException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    @Value("${JWT_ISSUER}")
    private String ISSUER;
    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;

    // Generate tokens
    public JwtVo generateTokens(User user) {
        // Payloads
        Map<String, Object> payloads = new LinkedHashMap<>();
        payloads.put("ID", user.getUserId());
        payloads.put("UUID", user.getUserUuid());

        // Expiration time (access - 1h / refresh - 7d)
        Date now = new Date();
        Date accessExp = new Date(now.getTime() + Duration.ofHours(1).toMillis());
        Date refreshExp = new Date(now.getTime() + Duration.ofDays(7).toMillis());

        String accessToken = builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(payloads)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(accessExp)
                .setSubject("ACCESS")
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes()))
                .compact();
        String refreshToken = builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(payloads)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(refreshExp)
                .setSubject("REFRESH")
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes()))
                .compact();

        return new JwtVo(accessToken, refreshToken);
    }

    // Decode header
    public String decodeHeader(String header) {
        try {
            return Arrays.stream(header.split("Bearer ")).toList().get(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AppException(GlobalErrorCode.INVALID_TOKEN);
        }
    }

    // Get payloads
    public Map<String, Object> getPayloads(String jwt) {
        return parser()
                .setSigningKey(JWT_SECRET_KEY.getBytes())
                .parseClaimsJws(jwt)
                .getBody();
    }

    // Validate tokens
    @Transactional(readOnly = true)
    public User validateToken(boolean isAccessToken, String header) throws AppException {
        // Decode header
        String token = decodeHeader(header);

        // Validate token
        // Get payload
        Map<String, Object> payloads = getPayloads(token);

        // Find user info
        User user = userRepository.findById(((Number) payloads.get("ID")).longValue())
                .orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

        // Find login info
        String refresh = redisUtil.opsForValueGet(user.getUserId() + "_refresh");

        if (refresh == null)
            throw new AppException(GlobalErrorCode.LOGIN_REQUIRED);
        else if (!isAccessToken && !refresh.equals(token))
            throw new AppException(GlobalErrorCode.AUTHORIZATION_FAILED);

        return user;
    }
}
