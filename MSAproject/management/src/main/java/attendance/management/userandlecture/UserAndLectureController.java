package attendance.management.userandlecture;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("userandlecture")
public class UserAndLectureController {

    private final UserAndLectureService userAndLectureService;

    @GetMapping("/findall")
    public ResponseEntity<List<UserAndLectureResponseListDto>> findAll(){
        List<UserAndLectureResponseListDto> userAndLectures = userAndLectureService.findAll();
        return ResponseEntity.ok(userAndLectures);
    }

    @GetMapping("/userlist")
    public ResponseEntity<UserAndLectureResponsePageDto> list(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestHeader("Authorization") String token
    ){
        Sort sort = Sort.by(Sort.Direction.DESC, "state")
                .and(Sort.by(Sort.Direction.DESC, "lecture.startDate"));
        Pageable pageable = PageRequest.of(pageNum, size, sort);

        UserAndLectureResponsePageDto userAndLectureResponsePageDto = userAndLectureService.userlist(pageable, token);

        return ResponseEntity.ok(userAndLectureResponsePageDto);
    }

}
