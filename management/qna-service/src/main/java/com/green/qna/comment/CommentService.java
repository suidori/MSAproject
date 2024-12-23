package com.green.qna.comment;

import com.green.qna.comment.entity.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentEntity> findAllByQnAboardId(Long qnAboardId) {
        List<CommentEntity> list = commentRepository.findAll();
        return list;
    }

    public CommentEntity save(CommentReqDto commentReqDto) {
        CommentEntity commentEntity = CommentEntity.builder()
               .comment(commentReqDto.getComment())
               .build();
        return commentRepository.save(commentEntity);
    }

    public void deleteById(Long idx) {
        commentRepository.deleteById(idx);
    }
}
