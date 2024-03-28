package com.naeun.naeun_server.domain.User.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
