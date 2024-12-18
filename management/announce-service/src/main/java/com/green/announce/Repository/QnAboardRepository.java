package com.green.announce.Repository;

import com.green.announce.Entity.QnAboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QnAboardRepository extends JpaRepository<QnAboard, Long> {


    Optional<QnAboard> findByIdx(Long idx);

    // 기본적인 페이징 처리
    Page<QnAboard> findAll(Pageable pageable);
    
    // 특정 사용자의 게시글만 조회
    Page<QnAboard> findByUser_Idx(Long userIdx, Pageable pageable);
    
    // QnAboardIdx가 null이거나 특정 값인 경우 조회
    @Query("SELECT q FROM QnAboard q WHERE q.idx = :boardIdx OR :boardIdx IS NULL")
    Page<QnAboard> findByQnAboardIdxOrNull(@Param("boardIdx") Long boardIdx, Pageable pageable);
    
    // 제목으로 검색
    Page<QnAboard> findByTitleContaining(String title, Pageable pageable);
    
    // 내용으로 검색
    Page<QnAboard> findByContentContaining(String content, Pageable pageable);

}
