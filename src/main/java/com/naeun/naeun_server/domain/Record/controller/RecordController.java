package com.naeun.naeun_server.domain.Record.controller;

import com.naeun.naeun_server.domain.Record.dto.NewRecordReqDto;
import com.naeun.naeun_server.domain.Record.dto.RecordAnalysisResDto;
import com.naeun.naeun_server.domain.Record.dto.RecordListItemDto;
import com.naeun.naeun_server.domain.Record.service.RecordService;
import com.naeun.naeun_server.domain.User.domain.User;
import com.naeun.naeun_server.global.common.DataResponseDto;
import com.naeun.naeun_server.global.common.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/record")
public class RecordController {
    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<ResponseDto> addNewRecord(@RequestAttribute("user") User user, @ModelAttribute @Valid NewRecordReqDto newRecordReqDto) {
        RecordAnalysisResDto recordAnalysisResDto = recordService.addNewRecord(user, newRecordReqDto);

        return ResponseEntity.status(201).body(DataResponseDto.of(recordAnalysisResDto, 201));
    }

    @GetMapping
    public ResponseEntity<ResponseDto> readRecordList(@RequestAttribute("user") User user) {
        ArrayList<RecordListItemDto> result = recordService.readRecordList(user);

        return ResponseEntity.ok(DataResponseDto.of(result, 200));
    }

    @GetMapping("/detail")
    public ResponseEntity<ResponseDto> readRecordDetail(@RequestAttribute("user") User user, @RequestParam("record") Long recordId) {
        RecordAnalysisResDto recordAnalysisResDto = recordService.readRecordDetail(user, recordId);

        return ResponseEntity.ok(DataResponseDto.of(recordAnalysisResDto, 200));
    }
}
