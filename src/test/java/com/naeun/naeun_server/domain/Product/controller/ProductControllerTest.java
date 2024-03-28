package com.naeun.naeun_server.domain.Product.controller;

import com.naeun.naeun_server.domain.User.domain.User;
import com.naeun.naeun_server.domain.User.domain.UserRepository;
import com.naeun.naeun_server.domain.model.JwtVo;
import com.naeun.naeun_server.global.util.JwtUtil;
import com.naeun.naeun_server.global.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
@Transactional
@Slf4j
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;

    private final String API_URL = "/api/v1/product";

    private User user;
    private String accessToken;

    @BeforeEach
    void addMockUser() {
        String mockId = "test-test36";
        user = userRepository.save(new User(UUID.randomUUID().toString().replace("-", ""), mockId));
        JwtVo jwtVo = jwtUtil.generateTokens(user);
        accessToken = jwtVo.getAccessToken();
        redisUtil.opsForValueSet(user.getUserId() + "_refresh", jwtVo.getRefreshToken(), 1);
    }

    @AfterEach
    void removeMockData() {
        redisUtil.delete(user.getUserId() + "_refresh");
    }

    @Test
    @DisplayName("상품 검색 성공")
    void searchProduct() throws Exception {
        // given
        String query = "하나";

        // when
        ResultActions resultActions = mockMvc.perform(get(API_URL + "/query")
                .header("Authorization", "Bearer " + accessToken)
                .param("query", query)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result[0].productId").exists())
                .andExpect(jsonPath("$.data.result[0].title").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void readProductDetail() throws Exception {
        // given
        Long productId = 7L;
        String expectedTitle = "트루(ELS) 16952";
        String expectedUnderlying1 = "EUROSTOXX50";

        // when
        ResultActions resultActions = mockMvc.perform(get(API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .param("product", String.valueOf(productId))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productId").value(7))
                .andExpect(jsonPath("$.data.title").value(expectedTitle))
                .andExpect(jsonPath("$.data.underlying[0]").value(expectedUnderlying1))
                .andDo(print());
    }
}