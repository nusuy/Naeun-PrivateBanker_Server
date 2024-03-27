package com.naeun.naeun_server.domain.User.service;

import com.naeun.naeun_server.domain.User.domain.User;
import com.naeun.naeun_server.domain.User.domain.UserRepository;
import com.naeun.naeun_server.domain.User.dto.LoginReqDto;
import com.naeun.naeun_server.domain.User.dto.LoginResDto;
import com.naeun.naeun_server.domain.model.JwtVo;
import com.naeun.naeun_server.global.util.JwtUtil;
import com.naeun.naeun_server.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    // Login
    @Transactional
    public LoginResDto login(LoginReqDto dto) {
        // Device id from Req
        String deviceId = dto.getId();

        // Check DB
        User user = userRepository.findByUserDeviceId(deviceId)
                .orElse(userRepository.save(new User(UUID.randomUUID().toString().replace("-", ""), deviceId)));

        // Generate Tokens
        JwtVo jwtVo = jwtUtil.generateTokens(user);

        // Save refresh token to Redis
        redisUtil.opsForValueSet(user.getUserId() + "_refresh", jwtVo.getRefreshToken(), 24 * 7);

        return new LoginResDto(user.getUserId(), jwtVo.getAccessToken(), jwtVo.getRefreshToken());
    }
}
