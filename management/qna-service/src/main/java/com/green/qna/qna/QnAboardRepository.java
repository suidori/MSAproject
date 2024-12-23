package com.green.qna.qna;

import com.green.qna.comment.entity.CommentEntity;
import com.green.qna.qna.entity.QnAboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnAboardRepository extends JpaRepository<QnAboard, Long> {

    // 기본적인 페이징 처리
    Page<QnAboard> findAll(Pageable pageable);
    
    Page<QnAboard> findByToken(String token, Pageable pageable);

    Page<QnAboard> findByuserid(String userid, Pageable pageable);

}
