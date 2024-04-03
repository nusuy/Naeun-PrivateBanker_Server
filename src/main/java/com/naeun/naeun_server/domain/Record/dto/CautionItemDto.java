package com.naeun.naeun_server.domain.Record.dto;

import com.naeun.naeun_server.domain.Record.domain.RecordCaution;
import lombok.Getter;

@Getter
public class CautionItemDto {
    private Long cautionId;
    private Long start;
    private Long end;
    private String reason;

    public CautionItemDto(RecordCaution recordCaution) {
        this.cautionId = recordCaution.getCautionId();
        this.start = recordCaution.getStartIndex();
        this.end = recordCaution.getEndIndex();
        this.reason = recordCaution.getCautionReason();
    }
}
