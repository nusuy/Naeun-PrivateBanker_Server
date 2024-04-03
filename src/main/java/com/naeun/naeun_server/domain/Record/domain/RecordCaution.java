package com.naeun.naeun_server.domain.Record.domain;

import com.naeun.naeun_server.domain.Record.dto.Gemini.ConvItemDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Table(name = "RecordCaution")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RecordCaution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cautionId;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecordDetail recordDetail;

    private Long startIndex;

    private Long endIndex;

    private String cautionReason;

    public RecordCaution(RecordDetail recordDetail, ConvItemDto itemDto) {
        this.recordDetail = recordDetail;
        this.startIndex = itemDto.getStart_index();
        this.endIndex = itemDto.getEnd_index();
        this.cautionReason = itemDto.getReason();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RecordCaution recordCaution)) return false;

        return Objects.equals(this.cautionId, recordCaution.getCautionId()) &&
                Objects.equals(this.recordDetail, recordCaution.getRecordDetail()) &&
                Objects.equals(this.startIndex, recordCaution.getStartIndex()) &&
                Objects.equals(this.endIndex, recordCaution.getEndIndex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cautionId, recordDetail, startIndex, endIndex);
    }
}
