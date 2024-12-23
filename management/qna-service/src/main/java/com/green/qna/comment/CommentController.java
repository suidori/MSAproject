package com.green.qna.comment;

import com.green.qna.comment.entity.CommentEntity;
import com.green.qna.feign.UserFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final UserFeignClient userFeignClient;

    @GetMapping("{qnAboardIdx}/list")
    @Operation(summary = "답글 리스트를 불러 옵니다.", description = "List comments")
    public ResponseEntity<List<CommentEntity>> list(@PathVariable Long qnAboardIdx) {
        return ResponseEntity.ok(commentService.findAllByQnAboardId(qnAboardIdx));
    }

    @PostMapping("{qnAboardIdx}/insert")
    public ResponseEntity<CommentEntity> insert(@PathVariable Long qnAboardIdx,@RequestBody CommentReqDto commentReqDto) {
        // openFeing user 정보 가져와야함
        commentReqDto.setQnaboard_idx(qnAboardIdx);
        return ResponseEntity.ok(commentService.save(commentReqDto));
    }

    @PutMapping("update")
    public ResponseEntity<CommentEntity> update(@RequestBody CommentReqDto commentReqDto) {
        return ResponseEntity.ok(commentService.save(commentReqDto));
    }

    // JWT -> userSerivec 우리가 발급 했는 JWT 맞냐..
    // UUID -> userService 우리한테 등록되어있는거 맞냐
    @DeleteMapping("delete/{idx}")
    public ResponseEntity<Void> delete(@PathVariable Long idx) {
//    public ResponseEntity<Void> delete(@PathVariable Long idx,
//                                       @RequestHeader("authorization") String authorization) {
//이게 성공하면... comment 삭제..
//        userFeignClient.getUser()
        commentService.deleteById(idx);
        return ResponseEntity.noContent().build();
    }
}
