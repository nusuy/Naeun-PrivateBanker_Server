package com.naeun.naeun_server.domain.Record.domain;

import com.naeun.naeun_server.domain.User.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "Record")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    private User user;

    private String recordTitle;

    private LocalTime recordLength;

    private LocalDate considerationExp;

    private Integer cautionCount;

    private String recordSummary;

    private String recordFilePath;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Record(User user, String recordTitle, LocalTime recordLength, String recordFilePath, LocalDate considerationExp) {
        this.user = user;
        this.recordTitle = recordTitle;
        this.recordLength = recordLength;
        this.recordFilePath = recordFilePath;
        this.considerationExp = considerationExp;
    }

    public void updateDataWithGemini(Integer cautionCount) {
        this.cautionCount = cautionCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Record record)) return false;

        return Objects.equals(this.recordId, record.getRecordId()) &&
                Objects.equals(this.user, record.getUser()) &&
                Objects.equals(this.recordLength, record.getRecordLength());
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId, user, recordLength);
    }
}
