package com.naeun.naeun_server.domain.Record.controller;

import com.naeun.naeun_server.domain.Record.domain.Record;
import com.naeun.naeun_server.domain.Record.domain.RecordRepository;
import com.naeun.naeun_server.domain.User.domain.User;
import com.naeun.naeun_server.domain.User.domain.UserRepository;
import com.naeun.naeun_server.domain.model.JwtVo;
import com.naeun.naeun_server.global.util.JwtUtil;
import com.naeun.naeun_server.global.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("prod")
@Slf4j
class RecordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    private final String API_URL = "/api/v1/record";
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

    void addMockRecords() {
        for (int i = 0; i < 3; i++) {
            recordRepository.save(Record.builder()
                    .user(user)
                    .recordTitle("test-data " + i)
                    .recordLength(LocalTime.parse("00:39:23"))
                    .recordFilePath("/test-data " + i + ".m4a")
                    .considerationExp(LocalDate.now().minusDays(i))
                    .build()
            );
        }
    }

    @Test
    @DisplayName("녹취 데이터 리스트 조회 성공")
    void readRecordList() throws Exception {
        // given
        addMockRecords();

        // when
        ResultActions resultActions = mockMvc.perform(get(API_URL)
                .header("Authorization", "Bearer " + accessToken));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("test-data 2"))
                .andDo(print());
    }

    @Test
    @DisplayName("녹취 데이터 추가 성공")
    void addNewRecord() throws Exception {
        // given
        String fileName = "test";
        String fileType = "audio/m4a";
        String filePath = "src/test/resources/" + fileName + ".m4a";
        FileInputStream audioFileInputStream = new FileInputStream(filePath);
        String title = "테스트 파일 제목";
        String length = "00:48:30";

        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                fileName + "." + fileType,
                fileType,
                audioFileInputStream);

        // when
        ResultActions resultActions = mockMvc.perform(multipart(API_URL)
                .file(audioFile)
                .param("title", title)
                .param("length", length)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(title))
                .andExpect(jsonPath("$.data.cautions").isArray())
                .andDo(print());
    }
}