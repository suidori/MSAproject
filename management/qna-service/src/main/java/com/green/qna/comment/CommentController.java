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
        return ResponseEntity.ok(commentService.findAllByQnAboardId(qnAboardIdx));
    }

    @PostMapping("{qnAboardIdx}/insert")
    public ResponseEntity<CommentEntity> insert(@PathVariable(value = "qnAboardIdx") Long qnAboardIdx
                                               ,@RequestBody CommentReqDto commentReqDto) {
        // openFeing user 정보 가져와야함
        System.out.println("패치밸리어블"+ qnAboardIdx);

        QnAboard qnAboard = qnAboardRepository.findById(qnAboardIdx).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

        qnAboard.setQnastate(QnAState.IN_PROGRESS);

        return ResponseEntity.ok(commentService.save(commentReqDto, qnAboard));
    }

//    @PutMapping("update")
//    public ResponseEntity<CommentEntity> update(@RequestBody CommentReqDto commentReqDto) {
//        return ResponseEntity.ok(commentService.save(commentReqDto));
//    }

    // JWT -> userSerivec 우리가 발급 했는 JWT 맞냐..
    // UUID -> userService 우리한테 등록되어있는거 맞냐
    @DeleteMapping("delete/{idx}")
    public ResponseEntity<CommentEntity> delete(@PathVariable(value = "idx") Long idx,
                                                @RequestHeader(name = "Authorization") String token) {
//    public ResponseEntity<Void> delete(@PathVariable Long idx,
//                                       @RequestHeader("authorization") String authorization) {
//이게 성공하면... comment 삭제..
//        userFeignClient.getUser()
        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

        CommentEntity comment = commentRepository.findById(idx).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

        if(!userReqDto.getUuid().equals(comment.getUuid())){

          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        commentService.deleteById(idx);
        log.info("[" + idx + "]"+ ": 코맨트 삭제 완료");
        return ResponseEntity.ok(comment);

    }
}
