package com.green.qna.Service;

import com.green.qna.Dto.QnACommentReqDto;
import com.green.qna.Dto.QnAboardReqDto;
import com.green.qna.Entity.QnAboard;
//import com.green.qna.Entity.User;
//import com.green.qna.Login.LoginUserDetails;
import com.green.qna.Repository.QnAboardRepository;
//import com.green.qna.Repository.UserRepository;
import com.green.qna.Response.QnAboardPageResponseDto;
import com.green.qna.Response.QnAboardResponseDto;
import com.green.qna.error.BizException;
import com.green.qna.error.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QnAboardService {

    private final ModelMapper modelMapper;
    private final QnAboardRepository qnAboardRepository;
//    private final UserRepository userRepository;

    public QnAboard save( QnAboardReqDto qnAboardReqDto) {

//        User user = userRepository.findByIdx(loginUserDetails.getIdx()).orElseThrow(()->new BizException(ErrorCode.USER_NOT_FOUND));

        QnAboard qnAboard = modelMapper.map(qnAboardReqDto, QnAboard.class);
        qnAboard.setWdate(LocalDateTime.now());

//        qnAboard.setUser(user);
        qnAboardRepository.save(qnAboard);
        return qnAboard;
    }

    
    public QnAboardPageResponseDto qnAPage(Pageable pageable) {

        Page<QnAboard> page = qnAboardRepository.findAll(pageable);

        return mapToQuestionResponsePageDto(page);
    }


    public QnAboardPageResponseDto qnAstudentPage(String user, Pageable pageable){

        Page<QnAboard> page = qnAboardRepository.findByUser(user,pageable);

        return mapToQuestionResponsePageDto(page);
    }


    private QnAboardPageResponseDto mapToQuestionResponsePageDto(Page<QnAboard> page) {
        // DTO 리스트 변환
        List<QnAboardResponseDto> dtoList = page.getContent().stream()
                .map(this::convertToQnAboardResponseDto)
                .toList();

        // 빈 페이지 처리 (필요에 따라 메시지나 다른 처리를 할 수 있음)
        if (dtoList.isEmpty()) {
            log.info("No data found in the page.");
        }

        // 페이지 응답 DTO 생성
        QnAboardPageResponseDto responseDto = new QnAboardPageResponseDto();
        responseDto.setList(dtoList);
        responseDto.setTotalElements(page.getTotalElements());
        responseDto.setTotalPages(page.getTotalPages());
        responseDto.setSize(page.getSize());
    
        return responseDto;
    }
    

    // QnAboard를 QnAboardResponseDto로 변환하는 메서드 분리
private QnAboardResponseDto convertToQnAboardResponseDto(QnAboard qnAboard) {
    QnAboardResponseDto dto = modelMapper.map(qnAboard, QnAboardResponseDto.class);
    
    // 날짜 포맷팅
    if (qnAboard.getWdate() != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        dto.setWdate(qnAboard.getWdate().format(formatter));
    }
    
    // 사용자 정보 설정 - Optional 대신 일반 null 체크 사용
//    dto.setUser(qnAboard.getUser() != null ? qnAboard.getUser().getName() : "탈퇴한 회원");
    
    return dto;
}



    public QnAboardResponseDto viewPage(long idx) {

        QnAboard qnAboard = qnAboardRepository.findById(idx).orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

        QnAboardResponseDto qnaboardResponseDto = modelMapper.map(qnAboard, QnAboardResponseDto.class);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        qnaboardResponseDto.setWdate(dateTimeFormatter.format(qnAboard.getWdate()));

//        qnaboardResponseDto.setUser((qnAboard.getUser() != null) ? qnAboard.getUser().getName() : "탈퇴한 회원");
//        qnaboardResponseDto.setLecture((announce.getLecture()!=null)? announce.getLecture().getTitle() : "전체");

        return qnaboardResponseDto;

    }

    public QnAboard addComment(long idx, @Valid QnACommentReqDto commentReqDto) {

        QnAboard qnAboard = qnAboardRepository.findById(idx).orElseThrow(() -> new RuntimeException("QnAboard not found"));

        // 댓글 내용 설정
        qnAboard.setComment(commentReqDto.getComment());

        // 댓글 작성자 설정
//        User commentUser = userRepository.findById(loginUserDetails.getIdx())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        qnAboard.setCommentuser(commentUser);

        // 댓글이 추가된 게시글 저장
        return qnAboardRepository.save(qnAboard);
    }
    }

