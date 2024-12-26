package com.green.qna.comment;

import com.green.qna.Dto.UserReqDto;
import com.green.qna.comment.entity.CommentEntity;
import com.green.qna.feign.UserFeignClient;
import com.green.qna.qna.entity.QnAboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserFeignClient userFeignClient;

    public List<CommentEntity> findAllByQnAboardId(Long qnAboardId) {
        List<CommentEntity> list = commentRepository.findByIdx(qnAboardId);
        return list;
    }

    public CommentEntity save(UserReqDto userReqDto ,String token, CommentReqDto commentReqDto, QnAboard qnAboard) {

        CommentEntity commentEntity = CommentEntity.builder()
               .comment(commentReqDto.getComment())
                .qnaboard(qnAboard)
                .wdate(LocalDateTime.now())
                .uuid(userReqDto.getUuid())
                .name(userReqDto.getName())
               .build();

        return commentRepository.save(commentEntity);
    }


    public void deleteById(Long idx) {
        commentRepository.deleteById(idx);
    }

}
