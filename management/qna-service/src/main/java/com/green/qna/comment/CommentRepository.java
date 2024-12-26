package com.green.qna.comment;

import com.green.qna.comment.entity.CommentEntity;
import com.green.qna.qna.entity.QnAboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    Optional<CommentEntity> deleteByqnaboard(QnAboard qnaboard);

    Optional<CommentEntity> findAllByqnaboard(QnAboard qnaboard);

    Optional<CommentEntity> findByuuid(QnAboard qnAboard);

    List<CommentEntity> findByIdx(Long qnAboardId);
}
