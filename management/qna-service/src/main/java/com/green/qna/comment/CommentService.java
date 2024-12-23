package com.green.qna.comment;

import com.green.qna.comment.entity.CommentEntity;
import com.green.qna.qna.entity.QnAboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentEntity> findAllByQnAboardId(Long qnAboardId) {
        List<CommentEntity> list = commentRepository.findAll();
        return list;
    }

    public CommentEntity save(CommentReqDto commentReqDto, QnAboard qnAboard) {
        CommentEntity commentEntity = CommentEntity.builder()
               .comment(commentReqDto.getComment())
                .qnaboard(qnAboard)
                .wdate(LocalDateTime.now())
               .build();
        return commentRepository.save(commentEntity);
    }

    public void deleteById(Long idx) {
        commentRepository.deleteById(idx);
    }
}
