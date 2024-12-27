package com.green.qna.comment;

import com.green.qna.Dto.UserReqDto;
import com.green.qna.comment.entity.CommentEntity;
import com.green.qna.feign.UserFeignClient;
import com.green.qna.qna.QnAboardRepository;
import com.green.qna.qna.entity.QnAState;
import com.green.qna.qna.entity.QnAboard;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserFeignClient userFeignClient;
    private final QnAboardRepository qnAboardRepository;

    public List<CommentEntity> findAllByQnAboardId(QnAboard qnAboard) {
        List<CommentEntity> list =  commentRepository.findByqnaboard(qnAboard);
        return list;
    }

    public CommentEntity save(UserReqDto userReqDto , CommentReqDto commentReqDto, QnAboard qnAboard) {

        CommentEntity commentEntity = CommentEntity.builder()
               .comment(commentReqDto.getComment())
                .qnaboard(qnAboard)
                .wdate(LocalDateTime.now())
                .uuid(userReqDto.getUuid())
                .name(userReqDto.getName())
                .role(userReqDto.getRole())
               .build();


        int comments = commentRepository.countByQnaboard(qnAboard);

        qnAboard.setComments(comments+1);
        qnAboard.setQnastate(QnAState.IN_PROGRESS);

        qnAboardRepository.save(qnAboard);

        return commentRepository.save(commentEntity);
    }

    public void deleteById(Long idx, QnAboard qnAboard) {

        int comments = qnAboard.getComments();

        comments --;

        if(comments==0){
            qnAboard.setQnastate(QnAState.WAITING);
        }
        qnAboard.setComments(comments);

        commentRepository.deleteById(idx);

    }
}
