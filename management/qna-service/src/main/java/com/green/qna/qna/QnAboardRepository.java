package com.green.qna.qna;

import com.green.qna.qna.entity.QnAboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnAboardRepository extends JpaRepository<QnAboard, Long> {

    // 기본적인 페이징 처리
    Page<QnAboard> findAll(Pageable pageable);

    Page<QnAboard> findByuserid(String userid, Pageable pageable);

    Page<QnAboard> findByuuid(String uuid, Pageable pageable);



    // 학생이 본인 작성 글만 검색
    @Query("SELECT q FROM QnAboard q WHERE q.uuid = :uuid AND " +
            "(LOWER(q.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(q.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<QnAboard> searchByUserAndQuery(@Param("uuid") String uuid,
                                        @Param("query") String query,
                                        Pageable pageable);

    // 관리자 및 선생은 전체 검색
    @Query("SELECT q FROM QnAboard q WHERE " +
            "LOWER(q.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(q.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<QnAboard> searchAll(@Param("query") String query, Pageable pageable);



    // 학생용


    @Query("SELECT q FROM QnAboard q WHERE q.type = :type AND q.uuid = :uuid")
    Page<QnAboard> findByTypeAndStudent(@Param("type") String type, @Param("uuid") String uuid, Pageable pageable);

    // 관리자 및 선생용

    Page<QnAboard> findByType(String type, Pageable pageable);
}








