package com.naeun.naeun_server.domain.User.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naeun.naeun_server.domain.User.dto.LoginReqDto;
import com.naeun.naeun_server.global.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Transactional
@Slf4j
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisUtil redisUtil;

    private final String API_URL = "/api/v1/user";

    @Test
    void login() throws Exception {
        // given
        String id = "test-45test";
        LoginReqDto dto = new LoginReqDto(id);

        // when
        ResultActions resultActions = mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andDo(print());
    }
}