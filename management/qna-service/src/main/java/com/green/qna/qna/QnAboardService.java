package com.green.qna.qna;

import com.green.qna.comment.CommentRepository;
import com.green.qna.Dto.UserReqDto;
import com.green.qna.qna.entity.QnAState;
import com.green.qna.qna.entity.QnAboard;
import com.green.qna.error.BizException;
import com.green.qna.error.ErrorCode;
import com.green.qna.feign.UserFeignClient;
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
    private final UserFeignClient userFeignClient;
    private final CommentRepository commentRepository;

    public QnAboard save(String token, QnAboardReqDto qnAboardReqDto, UserReqDto userReqDto) {

        QnAboard qnAboard = modelMapper.map(qnAboardReqDto, QnAboard.class);

        // uuid 오픈 페인으로 들어오면 추가 해야함.
//        qnAboard.setUuid(userReqDto.getUuid());
        qnAboard.setName(userReqDto.getName());
        qnAboard.setUserid(userReqDto.getUserid());
        qnAboard.setQnastate(QnAState.WAITING);
        qnAboard.setRole(userReqDto.getRole());
        qnAboard.setWdate(LocalDateTime.now());
//        qnAboard.setToken(token);
        qnAboard.setUuid(userReqDto.getUuid());

        qnAboardRepository.save(qnAboard);
        return qnAboard;
    }





    //서치 서비스 테스트
    public QnAboardPageResponseDto mapToPageResponseDto(Page<QnAboard> page) {
        List<QnAboardResponseDto> dtoList = page.getContent().stream()
                .map(this::convertToQnAboardResponseDto)
                .toList();

        return QnAboardPageResponseDto.builder()
                .list(dtoList)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .size(page.getSize())
                .build();
    }


    public QnAboardPageResponseDto qnAPage(Pageable pageable) {

        Page<QnAboard> page = qnAboardRepository.findAll(pageable);

        return mapToQuestionResponsePageDto(page);
    }


    public QnAboardPageResponseDto qnAstudentPage(String token, Pageable pageable) {

        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);

//        String userid = userReqDto.getUserid();
        String uuid = userReqDto.getUuid();

        Page<QnAboard> page = qnAboardRepository.findByuuid(uuid, pageable);

        return mapToQuestionResponsePageDto(page);
    }


    private QnAboardPageResponseDto mapToQuestionResponsePageDto(Page<QnAboard> page) {
        List<QnAboardResponseDto> dtoList = page.getContent().stream()
                .map(this::convertToQnAboardResponseDto)
                .toList();

        return QnAboardPageResponseDto.builder()
                .list(dtoList)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .size(page.getSize())
                .build();
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

    public QnAboard check(QnAboard qnAboard) {
        qnAboard.setQnastate(QnAState.COMPLETE);
        qnAboardRepository.save(qnAboard);
        return qnAboard;
    }





    public QnAboardPageResponseDto qnAPageWithType(String type, Pageable pageable) {
        Page<QnAboard> page;

        if ("구분".equals(type)) {
            page = qnAboardRepository.findAll(pageable);
        } else {
            page = qnAboardRepository.findByType(type, pageable);
        }

        // 비어 있는 페이지 처리
        if (page == null || page.isEmpty()) {
            page = Page.empty(pageable); // 빈 페이지 생성
        }

        return mapToQuestionResponsePageDto(page);
    }

    public QnAboardPageResponseDto qnAstudentPageWithType(String type, String token, Pageable pageable) {
        UserReqDto userReqDto = userFeignClient.getUser("Bearer " + token);
        String uuid = userReqDto.getUuid();

        Page<QnAboard> page;

        if ("구분".equals(type)) {
            page = qnAboardRepository.findByuuid(uuid, pageable);
        } else {
            page = qnAboardRepository.findByTypeAndStudent(type, uuid, pageable);
        }

        // 비어 있는 페이지 처리
        if (page == null || page.isEmpty()) {
            page = Page.empty(pageable); // 빈 페이지 생성
        }

        return mapToQuestionResponsePageDto(page);
    }

}




