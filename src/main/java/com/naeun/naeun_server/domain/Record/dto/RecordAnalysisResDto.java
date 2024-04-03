package com.naeun.naeun_server.domain.Record.dto;

import com.naeun.naeun_server.domain.Record.domain.Record;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Getter
public class RecordAnalysisResDto {
    private Long recordId;
    private LocalDate createdAt;
    private String title;
    private LocalTime length;
    private String content;
    private ArrayList<CautionItemDto> cautions;

    public RecordAnalysisResDto(Record record, String content, ArrayList<CautionItemDto> cautions) {
        this.recordId = record.getRecordId();
        this.createdAt = record.getCreatedAt().toLocalDate();
        this.title = record.getRecordTitle();
        this.length = record.getRecordLength();
        this.content = content;
        this.cautions = cautions;
    }
}
