package com.naeun.naeun_server.domain.Record.service;

import com.naeun.naeun_server.domain.Record.dto.NewRecordReqDto;
import com.naeun.naeun_server.domain.Record.dto.RecordAnalysisResDto;
import com.naeun.naeun_server.domain.User.domain.User;
import com.naeun.naeun_server.domain.User.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("prod")
class RecordServiceTest {
    @Autowired
    private RecordService recordService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void addMockUser() {
        user = userRepository.save(new User(UUID.randomUUID().toString().replace("-", ""), "test-45test"));
    }

    @Test
    @DisplayName("새로운 녹취 데이터 추가 성공")
    void addNewRecord() throws Exception {
        // given
        String fileName = "test";
        String fileType = "m4a";
        String filePath = "src/test/resources/" + fileName + "." + fileType;
        FileInputStream audioFileInputStream = new FileInputStream(filePath);
        String title = "테스트 파일 제목";
        String length = "00:48:30";

        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                fileName + "." + fileType,
                fileType,
                audioFileInputStream);

        // when
        RecordAnalysisResDto recordAnalysisResDto = recordService.addNewRecord(user, new NewRecordReqDto(audioFile, title, length));

        // then
        assertThat(recordAnalysisResDto.getTitle()).isEqualTo(title);
    }
}