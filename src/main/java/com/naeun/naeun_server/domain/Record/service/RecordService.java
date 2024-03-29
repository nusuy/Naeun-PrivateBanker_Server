package com.naeun.naeun_server.domain.Record.service;

import com.naeun.naeun_server.domain.Record.domain.Record;
import com.naeun.naeun_server.domain.Record.domain.RecordDetail;
import com.naeun.naeun_server.domain.Record.domain.RecordDetailRepository;
import com.naeun.naeun_server.domain.Record.domain.RecordRepository;
import com.naeun.naeun_server.domain.Record.dto.NewRecordReqDto;
import com.naeun.naeun_server.domain.Record.dto.RecordAnalysisResDto;
import com.naeun.naeun_server.domain.Record.dto.RecordListItemDto;
import com.naeun.naeun_server.domain.Record.error.RecordErrorCode;
import com.naeun.naeun_server.domain.User.domain.User;
import com.naeun.naeun_server.global.error.GlobalErrorCode;
import com.naeun.naeun_server.global.error.exception.AppException;
import com.naeun.naeun_server.global.util.GcsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final RecordDetailRepository recordDetailRepository;
    private final GcsUtil gcsUtil;

    @Transactional
    public RecordAnalysisResDto addNewRecord(User user, NewRecordReqDto newRecordReqDto) {
        LocalDateTime now = LocalDateTime.now();
        String ext = newRecordReqDto.getFile().getContentType();
        String fileName = user.getUserId() + "_" + now + "." + ext;

        // Get file length & add record file to bucket
        String path;
        try {
            path = gcsUtil.uploadFile(newRecordReqDto.getFile(), fileName, ext);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new AppException(RecordErrorCode.FILE_UPLOAD_FAILED);
        }

        // Calculate consideration exp (after 2 business days)
        LocalDate exp;
        if (now.getDayOfWeek().getValue() < 4) {
            exp = now.toLocalDate().plusDays(2);
        } else {
            int count = 0;
            exp = now.toLocalDate();
            while (count < 2) {
                exp = exp.plusDays(1);
                if (exp.getDayOfWeek().getValue() < 6)
                    count++;
            }
        }

        // Add record entity
        Record record = Record.builder()
                .user(user)
                .recordTitle(newRecordReqDto.getTitle())
                .recordLength(LocalTime.parse(newRecordReqDto.getLength()))
                .recordFilePath(path)
                .considerationExp(exp)
                .build();

        RecordDetail dummy = recordDetailRepository.findById(1L)
                .orElseThrow(() -> new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));
        String content = dummy.getDetailContent();

        // Request Gemini API

        // Add data to entity

        // Add entity to DB
        record = recordRepository.save(record);

        return new RecordAnalysisResDto(record, content, null);
    }

    @Transactional(readOnly = true)
    public ArrayList<RecordListItemDto> readRecordList(User user) {
        return recordRepository.findAllByUserWithJPQL(user);
    }
}
