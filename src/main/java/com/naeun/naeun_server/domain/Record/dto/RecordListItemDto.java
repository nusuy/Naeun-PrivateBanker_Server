package com.naeun.naeun_server.domain.Record.dto;

import com.naeun.naeun_server.domain.Record.domain.Record;
import lombok.Getter;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
public class RecordListItemDto {
    private Long recordId;
    private String title;
    private Integer cautions;
    private Integer remainingDay;

    public RecordListItemDto(Record record) {
        this.recordId = record.getRecordId();
        this.title = record.getRecordTitle();
        this.cautions = record.getCautionCount();
        long days = DAYS.between(LocalDate.now(), record.getConsiderationExp()) + 1;
        this.remainingDay = LocalDate.now().isAfter(record.getConsiderationExp()) ? 0 : (int) days;
    }
}
