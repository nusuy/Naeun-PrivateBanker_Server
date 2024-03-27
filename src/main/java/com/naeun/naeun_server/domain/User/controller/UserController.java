package com.naeun.naeun_server.domain.User.controller;

import com.naeun.naeun_server.domain.User.dto.LoginReqDto;
import com.naeun.naeun_server.domain.User.dto.LoginResDto;
import com.naeun.naeun_server.domain.User.service.UserService;
import com.naeun.naeun_server.global.common.DataResponseDto;
import com.naeun.naeun_server.global.common.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDto> login(@RequestBody @Valid LoginReqDto loginReqDto) {
        LoginResDto loginResDto = userService.login(loginReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(loginResDto, 201));
    }
}
