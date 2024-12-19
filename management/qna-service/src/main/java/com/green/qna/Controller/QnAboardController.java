package com.green.qna.Controller;

import com.green.qna.Dto.QnACommentReqDto;
import com.green.qna.Dto.QnAboardReqDto;
import com.green.qna.Dto.UserReqDto;
import com.green.qna.Entity.QnAboard;
import com.green.qna.Repository.QnAboardRepository;
import com.green.qna.Response.QnAboardPageResponseDto;
import com.green.qna.Response.QnAboardResponseDto;
import com.green.qna.Service.QnAboardService;
import com.green.qna.feign.UserFeignClient;
import com.green.qna.utility.PageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("QnA")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class QnAboardController {

    private final QnAboardRepository qnAboardRepository;
    private final QnAboardService qnAboardService;
    private final UserFeignClient userFeignClient;

    @GetMapping("/list")
    public ResponseEntity<QnAboardPageResponseDto> test(String token,
                                                        @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        System.out.println("프론트에서 준 token " + token);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        // 선생이거나 매니저면.. 모든 select...
        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

        System.out.println("토큰으로 불러온 유저 정보 " + userReqDto);

        if (userReqDto.getRole().equals("ROLE_STUDENT")) {
            //token 가지고... 학생일때는 가져올게 없는데.. 자기꺼만 select..
            QnAboardPageResponseDto studentQnAList =
                    qnAboardService.qnAstudentPage(token, PageUtil.getPageable(pageNum, size));
            return ResponseEntity.ok(studentQnAList);
        } else {
            QnAboardPageResponseDto qnAboardPageResponseDto =
                    qnAboardService.qnAPage(PageUtil.getPageable(pageNum, size));
            return ResponseEntity.ok(qnAboardPageResponseDto);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<QnAboard> save(
            @RequestHeader(name = "Authorization") String token,
            @Valid @RequestBody QnAboardReqDto qnAboardReqDto) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println(token);

        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

        System.out.println("세이브 토큰으로 불러온거 "+userReqDto);

//        QnAboard qnAboard = qnAboardService.save(token.split("Bearer")[1],qnAboardReqDto);
        QnAboard qnAboard = qnAboardService.save(token ,qnAboardReqDto ,userReqDto);
        return ResponseEntity.ok(qnAboard);
    }


    @GetMapping("view/{idx}")
    public ResponseEntity<QnAboardResponseDto> findOne(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "idx") long idx) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        QnAboardResponseDto qnAboardResponseDto = qnAboardService.viewPage(idx);
        return ResponseEntity.ok(qnAboardResponseDto);
    }

    @PostMapping("/comment/{idx}")
    public ResponseEntity<QnAboard> addComment(
            String token,
            @PathVariable(name = "idx") long idx,
            @Valid @RequestBody QnACommentReqDto commentReqDto) {


        System.out.println("댓글 토큰"+ token);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        QnAboard qnAboard = qnAboardService.addComment(idx, token,  commentReqDto);

        return ResponseEntity.ok(qnAboard);

    }


}
