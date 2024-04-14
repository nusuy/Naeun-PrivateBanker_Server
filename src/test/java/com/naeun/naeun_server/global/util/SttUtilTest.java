package com.naeun.naeun_server.global.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("prod")
@Slf4j
class SttUtilTest {
    @Autowired
    private SttUtil sttUtil;

    @Test
    @DisplayName("SST m4a 파일 텍스트 변환 성공")
    void transcribe() throws Exception {
        // given
        String fileName = "test";
        String fileType = "flac";
        String filePath = "src/test/resources/" + fileName + "." + fileType;
        FileInputStream audioFileInputStream = new FileInputStream(filePath);

        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                fileName + "." + fileType,
                fileType,
                audioFileInputStream);

        // when
        String text = sttUtil.transcribe(audioFile);

        // then
        assertThat(text).as("텍스트 변환 실패").isNotNull();
        log.info(text);
    }
}