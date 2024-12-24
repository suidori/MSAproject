package com.green.qna.qna;

import com.green.qna.comment.CommentRepository;
import com.green.qna.comment.CommentReqDto;
import com.green.qna.Dto.UserReqDto;
import com.green.qna.qna.entity.QnAboard;
import com.green.qna.feign.UserFeignClient;
import com.green.qna.utility.PageUtil;
import jakarta.persistence.EntityNotFoundException;
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
    private final CommentRepository commentRepository;

    // 학생이면 본인이 작성한 list
    // 매니저나 선생이면 ALL list

    //토큰이 아니라 uuid로 변경할것...
    @GetMapping("/list")
    public ResponseEntity<QnAboardPageResponseDto> test(String token,
                                                        @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        System.out.println("프론트에서 준 token " + token);

//        if (token == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

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

    @PostMapping("/qnacheck/{idx}")
    public String check(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "idx") long idx
            ) {

        QnAboard qnAboard = qnAboardRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("QnA idx일치하는게 없음 " + idx));

        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

        if(userReqDto.getUuid()!=qnAboard.getUuid()){

            return "요청자의 idx가 일치하지 않습니다.";
        }

        System.out.println("세이브 토큰으로 불러온거 "+userReqDto);

        qnAboardService.check(qnAboard);

        return "문의완료 체크";
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

    @DeleteMapping("delete/{idx}")
    public ResponseEntity<QnAboard> qnadelete (@RequestParam(value = "idx") Long idx,
                                               @RequestHeader(name = "Authorization") String token) {

        QnAboard qnAboard = qnAboardRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("QnA idx일치하는게 없음 " + idx));
        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

        if(userReqDto.getUserid().equals(qnAboard.getUserid())){

            if(commentRepository.findAllByqnaboard(qnAboard)!=null){
                commentRepository.deleteByqnaboard(qnAboard);
            }
            qnAboardRepository.deleteById(idx);
        }
        return ResponseEntity.ok(qnAboard);
    }

}
