package com.green.qna.Repository;

import com.green.qna.Entity.QnAboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QnAboardRepository extends JpaRepository<QnAboard, Long> {

    // 기본적인 페이징 처리
    Page<QnAboard> findAll(Pageable pageable);
    
    Page<QnAboard> findByToken(String token, Pageable pageable);

}
