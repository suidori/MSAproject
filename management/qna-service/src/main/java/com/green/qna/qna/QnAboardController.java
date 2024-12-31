package com.green.qna.qna;

import com.green.qna.comment.CommentRepository;
import com.green.qna.Dto.UserReqDto;
import com.green.qna.comment.entity.CommentEntity;
import com.green.qna.qna.entity.QnAboard;
import com.green.qna.feign.UserFeignClient;
import com.green.qna.utility.PageUtil;
import com.sun.tools.jconsole.JConsoleContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(summary = "QnA 리스트를 불러옵니다.",
            parameters = {
                    @Parameter(name = "type", description = "구분", required = true),
                    @Parameter(name = "token", description = "인증 토큰", required = true),
                    @Parameter(name = "pageNum", description = "페이지 번호", required = true),
                    @Parameter(name = "size", description = "페이지 크기", required = false)
            })
    public ResponseEntity<QnAboardPageResponseDto> qetlist(String token,
                                                           @RequestParam(name = "type", defaultValue = "구분") String type,
                                                        @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        System.out.println("프론트에서 준 token " + token);
        System.out.println("type" + type);
        System.out.println("pagenum" + pageNum );
        System.out.println("size" + size );
//        if (token == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        // 선생이거나 매니저면.. 모든 select...
        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);
        Pageable pageable = PageUtil.getPageable(pageNum, size);

        QnAboardPageResponseDto responseDto;

        if ("ROLE_STUDENT".equals(userReqDto.getRole())) {
            responseDto = qnAboardService.qnAstudentPageWithType(type, token, pageable);
        } else {
            responseDto = qnAboardService.qnAPageWithType(type, pageable);
        }

        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/save")
    @Operation(summary = "QnA를 저장합니다.")
    public ResponseEntity<QnAboard> save(
            @RequestHeader(name = "Authorization") String token,
            @Valid @RequestBody QnAboardReqDto qnAboardReqDto) {

        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

//        QnAboard qnAboard = qnAboardService.save(token.split("Bearer")[1],qnAboardReqDto);
        QnAboard qnAboard = qnAboardService.save(token ,qnAboardReqDto ,userReqDto);
        return ResponseEntity.ok(qnAboard);
    }

    @PostMapping("/change/{idx}")
    @Operation(summary = "QnA를 수정합니다.")
    public ResponseEntity<QnAboard> change(
            @PathVariable(name = "idx") long idx,
            @RequestHeader(name = "Authorization") String token,
            @Valid @RequestBody QnAboardReqDto qnAboardReqDto) {

        UserReqDto userReqDto = userFeignClient.getUser(token);

//        QnAboard qnAboard = qnAboardService.save(token.split("Bearer")[1],qnAboardReqDto);

        QnAboard qnAboard = qnAboardRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("QnA idx일치하는게 없음 " + idx));

        qnAboard.setTitle(qnAboardReqDto.getTitle());
        qnAboard.setContent(qnAboardReqDto.getContent());
        qnAboard.setType(qnAboardReqDto.getType());

        System.out.println("변경된거"+qnAboard);

        qnAboardRepository.save(qnAboard);

        return ResponseEntity.ok(qnAboard);
    }


    @PostMapping("/qnacheck/{idx}")
    @Operation(summary = "문의완료 체크")
    public ResponseEntity<QnAboard> check(
            @PathVariable(name = "idx") long idx,
             @RequestHeader(name = "Authorization") String token
            ){

        QnAboard qnAboard = qnAboardRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("QnA idx일치하는게 없음 " + idx));

        UserReqDto userReqDto = userFeignClient.getUser(token);

        if(userReqDto.getUuid().equals(qnAboard.getUuid())){

            qnAboardService.check(qnAboard);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(qnAboard);
    }

    @GetMapping("view/{idx}")
    @Operation(summary = "QnA를 열람합니다.")
    public ResponseEntity<QnAboardResponseDto> findOne(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "idx") long idx) {

        QnAboardResponseDto qnAboardResponseDto = qnAboardService.viewPage(idx);
        return ResponseEntity.ok(qnAboardResponseDto);
    }

    @DeleteMapping("delete/{idx}")
    @Operation(summary = "QnA를 삭제합니다.")
    public String qnadelete (@PathVariable(name = "idx") long idx,
                             @RequestHeader(name = "Authorization") String token) {

        QnAboard qnAboard = qnAboardRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("QnA idx일치하는게 없음 " + idx));
        UserReqDto userReqDto = userFeignClient.getUser(token);

        if(userReqDto.getUuid().equals(qnAboard.getUuid())){
            List<CommentEntity> list =  commentRepository.findByqnaboard(qnAboard);
            if (!list.isEmpty()) {
                commentRepository.deleteAll(list);
            }
            qnAboardRepository.deleteById(idx);
        }
        return "삭제완료";
    }

   // 서치 메서드
    @GetMapping("/search")
    @Operation(summary = "QnA 검색(테스트)")
    public ResponseEntity<QnAboardPageResponseDto> searchQnA(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // 유저 정보 조회
        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

        System.out.println("검색 요청자: " + userReqDto);

        // 검색 수행
        Page<QnAboard> resultPage;
        if ("ROLE_STUDENT".equals(userReqDto.getRole())) {
            // 학생인 경우 본인 작성글만 검색
            resultPage = qnAboardRepository.searchByUserAndQuery(
                    userReqDto.getUuid(), query, PageUtil.getPageable(pageNum, size));

        } else {
            // 관리자나 선생은 전체 검색
            resultPage = qnAboardRepository.searchAll(query, PageUtil.getPageable(pageNum, size));

        }
        // 검색 결과 매핑
        QnAboardPageResponseDto responseDto = qnAboardService.mapToPageResponseDto(resultPage);

        return ResponseEntity.ok(responseDto);
    }
}
