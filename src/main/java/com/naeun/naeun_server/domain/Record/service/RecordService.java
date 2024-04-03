package com.naeun.naeun_server.domain.Record.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naeun.naeun_server.domain.Record.domain.Record;
import com.naeun.naeun_server.domain.Record.domain.*;
import com.naeun.naeun_server.domain.Record.dto.CautionItemDto;
import com.naeun.naeun_server.domain.Record.dto.Gemini.ConvAIResDto;
import com.naeun.naeun_server.domain.Record.dto.Gemini.ConvItemDto;
import com.naeun.naeun_server.domain.Record.dto.Gemini.ConvReqDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    private final RecordCautionRepository recordCautionRepository;
    private final GcsUtil gcsUtil;

    @Value("${FAST_API_BASE_URL}")
    private String FAST_API_BASE_URL;

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

        // Dummy data
        RecordDetail dummy = recordDetailRepository.findById(1L)
                .orElseThrow(() -> new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));
        String content = dummy.getDetailContent();

        // Request Gemini API
        ConvAIResDto resDto;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ConvReqDto convReqDto = new ConvReqDto(content);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(new ObjectMapper().writeValueAsString(convReqDto), headers);

            resDto = restTemplate.postForObject(
                    FAST_API_BASE_URL + "/api/v1/conv-analysis",
                    httpEntity,
                    ConvAIResDto.class);

            if (resDto == null)
                throw new AppException(RecordErrorCode.FAST_API_REQ_FAILED);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            throw new AppException(RecordErrorCode.FAST_API_REQ_FAILED);
        }

        // Add data to entity
        record.updateDataWithGemini(resDto.getResult().size());
        record = recordRepository.save(record);
        RecordDetail recordDetail = recordDetailRepository.save(new RecordDetail(record, content));

        ArrayList<CautionItemDto> cautions = new ArrayList<>();

        for (ConvItemDto dto : resDto.getResult())
            cautions.add(
                    new CautionItemDto(
                            recordCautionRepository.save(
                                    new RecordCaution(recordDetail, dto))
                    )
            );

        return new RecordAnalysisResDto(record, content, cautions);
    }

    @Transactional(readOnly = true)
    public ArrayList<RecordListItemDto> readRecordList(User user) {
        return recordRepository.findAllByUserWithJPQL(user);
    }

    public RecordAnalysisResDto readRecordDetail(User user, Long recordId) {
        // Check record authority
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new AppException(RecordErrorCode.RECORD_NOT_FOUND));
        if (!record.getUser().equals(user))
            throw new AppException(RecordErrorCode.RECORD_ACCESS_FORBIDDEN);

        // Find detail (dummy)
        RecordDetail recordDetail = recordDetailRepository.findById(recordId)
                .orElseThrow(() -> new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));

        // Find cautions
        ArrayList<CautionItemDto> cautionItemList = recordCautionRepository.findAllByRecordDetailWithJPQL(recordDetail);

        return new RecordAnalysisResDto(record, recordDetail.getDetailContent(), cautionItemList);
    }
}
