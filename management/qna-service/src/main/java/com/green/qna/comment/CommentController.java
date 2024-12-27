package com.green.qna.comment;

import com.green.qna.Dto.UserReqDto;
import com.green.qna.comment.entity.CommentEntity;
import com.green.qna.error.BizException;
import com.green.qna.error.ErrorCode;
import com.green.qna.feign.UserFeignClient;
import com.green.qna.qna.QnAboardRepository;
import com.green.qna.qna.QnAboardReqDto;
import com.green.qna.qna.entity.QnAState;
import com.green.qna.qna.entity.QnAboard;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class CommentController {

    private final QnAboardRepository qnAboardRepository;
    private final CommentService commentService;
    private final UserFeignClient userFeignClient;
    private final CommentRepository commentRepository;

    @GetMapping("{qnAboardIdx}/list")
    @Operation(summary = "답글 리스트를 불러 옵니다.", description = "List comments")
    public ResponseEntity<List<CommentEntity>> list(@PathVariable(value = "qnAboardIdx") Long qnAboardIdx) {

        QnAboard qnAboard = qnAboardRepository.findById(qnAboardIdx).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

        return ResponseEntity.ok(commentService.findAllByQnAboardId(qnAboard));
    }

    @PostMapping("{qnAboardIdx}/insert")
    @Operation(summary = "댓글을 저장합니다.")
    public ResponseEntity<CommentEntity> insert(@RequestHeader(name = "Authorization") String token
                                                ,@PathVariable(value = "qnAboardIdx") Long qnAboardIdx
                                               ,@RequestBody CommentReqDto commentReqDto) {
        // openFeing user 정보 가져와야함
        try {

            System.out.println("패치밸리어블" + qnAboardIdx);

//            UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

            UserReqDto userReqDto = userFeignClient.getUser(token);

            QnAboard qnAboard = qnAboardRepository.findById(qnAboardIdx).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

            qnAboard.setQnastate(QnAState.IN_PROGRESS);
            qnAboardRepository.save(qnAboard);

            return ResponseEntity.ok(commentService.save(userReqDto , commentReqDto, qnAboard));

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("delete/{idx}")
    @Operation(summary = "댓글을 삭제합니다.")
    public String delete(@PathVariable(value = "idx") Long idx,
                                                @RequestHeader(name = "Authorization") String token) {

        UserReqDto userReqDto = userFeignClient.getUser(token);

        CommentEntity comment = commentRepository.findById(idx).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

        if(!userReqDto.getUuid().equals(comment.getUuid())){

          return "댓글삭제 실패(uuid 불일치)";
        }
        commentService.deleteById(idx);
        log.info("[" + idx + "]"+ ": 코맨트 삭제 완료");
        return idx + " 댓글삭제 성공";
    }
}
