package com.naeun.naeun_server.domain.Record.domain;

import com.naeun.naeun_server.domain.Record.dto.CautionItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RecordCautionRepository extends JpaRepository<RecordCaution, Long> {
    @Query("select new com.naeun.naeun_server.domain.Record.dto.CautionItemDto(rc) " +
            "from RecordCaution rc " +
            "where rc.recordDetail = :recordDetail " +
            "order by rc.startIndex asc")
    ArrayList<CautionItemDto> findAllByRecordDetailWithJPQL(RecordDetail recordDetail);
}
