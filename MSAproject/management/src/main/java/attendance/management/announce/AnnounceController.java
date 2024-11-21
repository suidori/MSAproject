package attendance.management.announce;

import attendance.management.sign.LoginUserDetails;
import attendance.management.utility.PageUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("announce")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class AnnounceController {

    private final AnnounceService announceService;

    @PostMapping("save")
    public ResponseEntity<Announce> save(@AuthenticationPrincipal LoginUserDetails loginUserDetails, @Valid @RequestBody AnnounceReqDto announceReqDto) {
        if(loginUserDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Announce announce = announceService.save(loginUserDetails, announceReqDto);
        return ResponseEntity.ok(announce);
    }

    @GetMapping("view/{idx}")
    public ResponseEntity<AnnounceResponseDto> findOne(@PathVariable(name = "idx") long idx) {
        AnnounceResponseDto announceResponseDto = announceService.viewPage(idx);
        return ResponseEntity.ok(announceResponseDto);
    }

    @GetMapping("/student")
    public ResponseEntity<AnnounceResponsePageDto> studentFindAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        if(loginUserDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnounceResponsePageDto announceResponsePageDto = announceService.studentPage(PageUtil.getPageable(pageNum, size), loginUserDetails);
        return ResponseEntity.ok(announceResponsePageDto);
    }

    @GetMapping("/teacher")
    public ResponseEntity<AnnounceResponsePageDto> teacherFindAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        if(loginUserDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnounceResponsePageDto announceResponsePageDto = announceService.teacherPage(PageUtil.getPageable(pageNum, size), loginUserDetails);
        return ResponseEntity.ok(announceResponsePageDto);
    }

    @GetMapping("/teacherdesc")
    public ResponseEntity<AnnounceResponsePageDto> teacherFindAlldesc(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        if(loginUserDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AnnounceResponsePageDto announceResponsePageDto = announceService.teacherPage(PageUtil.getPageableASC(pageNum, size), loginUserDetails);
        return ResponseEntity.ok(announceResponsePageDto);
    }

    @GetMapping("/manager")
    public ResponseEntity<AnnounceResponsePageDto> managerFindAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        AnnounceResponsePageDto announceResponsePageDto = announceService.managerPage(PageUtil.getPageable(pageNum, size));
        return ResponseEntity.ok(announceResponsePageDto);
    }

    @GetMapping("/managerdesc")
    public ResponseEntity<AnnounceResponsePageDto> managerFindAlldesc(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        AnnounceResponsePageDto announceResponsePageDto = announceService.managerPage(PageUtil.getPageableASC(pageNum, size));
        return ResponseEntity.ok(announceResponsePageDto);
    }

    @GetMapping("/lecturesearch/{idx}")
    public ResponseEntity<AnnounceResponsePageDto> lectureFindAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @PathVariable(name = "idx") long idx
    ) {
        AnnounceResponsePageDto announceResponsePageDto = announceService.lectureSearch(PageUtil.getPageable(pageNum, size), idx);
        return ResponseEntity.ok(announceResponsePageDto);
    }

    @GetMapping("/lecturesearchdesc/{idx}")
    public ResponseEntity<AnnounceResponsePageDto> lectureFindAlldesc(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @PathVariable(name = "idx") long idx
    ) {
        AnnounceResponsePageDto announceResponsePageDto = announceService.lectureSearch(PageUtil.getPageableASC(pageNum, size), idx);
        return ResponseEntity.ok(announceResponsePageDto);
    }

    @GetMapping("/searchforall")
    public ResponseEntity<AnnounceResponsePageDto> searchAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        AnnounceResponsePageDto announceResponsePageDto = announceService.allSearch(PageUtil.getPageable(pageNum, size));
        return ResponseEntity.ok(announceResponsePageDto);
    }

    @GetMapping("/searchforalldesc")
    public ResponseEntity<AnnounceResponsePageDto> searchAlldesc(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        AnnounceResponsePageDto announceResponsePageDto = announceService.allSearch(PageUtil.getPageableASC(pageNum, size));
        return ResponseEntity.ok(announceResponsePageDto);
    }

}
