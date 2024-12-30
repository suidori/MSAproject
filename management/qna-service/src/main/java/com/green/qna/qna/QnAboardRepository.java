package com.green.qna.qna;

import com.green.qna.qna.entity.QnAboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnAboardRepository extends JpaRepository<QnAboard, Long> {

    Page<QnAboard> findByuserid(String userid, Pageable pageable);

    Page<QnAboard> findByuuid(String uuid, Pageable pageable);

    // 학생 사용자 - 전체
    Page<QnAboard> findAllByStudent(String token, Pageable pageable);

    // 학생 사용자 - 타입별 필터
    Page<QnAboard> findByTypeAndStudent(String type, String token, Pageable pageable);

    // 관리자 및 선생님 - 전체
    Page<QnAboard> findAll(Pageable pageable);

    // 관리자 및 선생님 - 타입별 필터
    Page<QnAboard> findByType(String type, Pageable pageable);

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

}
