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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("QnA")
@RequiredArgsConstructor
@CrossOrigin
public class QnAboardController {

    private final QnAboardRepository qnAboardRepository;
    private final QnAboardService qnAboardService;
    private final UserFeignClient userFeignClient;

    @GetMapping("/list")
    public ResponseEntity<QnAboardPageResponseDto> test(String token,
                                               @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        System.out.println(token);

        // 선생이거나 매니저면.. 모든 select...
        UserReqDto userReqDto = userFeignClient.getUser("Bearer "+token);

        if(userReqDto.getRole().equals("ROLE_STUDENT")){
            //token 가지고... 학생일때는 가져올게 없는데.. 자기꺼만 select..
            return ResponseEntity.ok(null);
        }
        else{
            QnAboardPageResponseDto qnAboardPageResponseDto =
                    qnAboardService.qnAstudentPage(PageUtil.getPageable(pageNum, size));
            return ResponseEntity.ok(qnAboardPageResponseDto);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<QnAboard> save(@Valid @RequestBody QnAboardReqDto qnAboardReqDto) {
//        if(loginUserDetails==null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        QnAboard qnAboard = qnAboardService.save(loginUserDetails, qnAboardReqDto);
//        return ResponseEntity.ok(qnAboard);
        return ResponseEntity.ok(null);
    }


    @GetMapping("view/{idx}")
    public ResponseEntity<QnAboardResponseDto> findOne(@PathVariable(name = "idx") long idx) {
        QnAboardResponseDto qnAboardResponseDto = qnAboardService.viewPage(idx);
        return ResponseEntity.ok(qnAboardResponseDto);
    }

    @PutMapping("/comment/{idx}")
    public ResponseEntity<QnAboard> addComment(
                                               @PathVariable(name = "idx") long idx,
                                               @Valid @RequestBody QnACommentReqDto commentReqDto) {
//        if (loginUserDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        QnAboard updatedQnAboard = qnAboardService.addComment(idx, loginUserDetails, commentReqDto);

//        return ResponseEntity.ok(updatedQnAboard);
        return ResponseEntity.ok(null);
    }


}
