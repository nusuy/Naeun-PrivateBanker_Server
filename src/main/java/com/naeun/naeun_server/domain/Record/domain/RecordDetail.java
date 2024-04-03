package com.naeun.naeun_server.domain.Record.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Table(name = "RecordDetail")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RecordDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Record record;

    @Column(columnDefinition = "TEXT")
    private String detailContent;

    public RecordDetail(Record record, String detailContent) {
        this.record = record;
        this.detailContent = detailContent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RecordDetail recordDetail)) return false;

        return Objects.equals(this.detailId, recordDetail.getDetailId()) &&
                Objects.equals(this.record, recordDetail.getRecord());
    }

    @Override
    public int hashCode() {
        return Objects.hash(detailId, record);
    }
}
