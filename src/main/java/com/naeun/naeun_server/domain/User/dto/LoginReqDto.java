package com.naeun.naeun_server.domain.User.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDto {
    @NotBlank(message = "Device id required.")
    private String id;
}
