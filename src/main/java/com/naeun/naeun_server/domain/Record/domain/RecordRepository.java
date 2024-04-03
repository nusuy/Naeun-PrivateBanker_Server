package com.naeun.naeun_server.domain.Record.domain;

import com.naeun.naeun_server.domain.Record.dto.RecordListItemDto;
import com.naeun.naeun_server.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("select new com.naeun.naeun_server.domain.Record.dto.RecordListItemDto(r) " +
            "from Record r " +
            "where r.user = :user " +
            "order by r.createdAt desc")
    ArrayList<RecordListItemDto> findAllByUserWithJPQL(User user);
}
