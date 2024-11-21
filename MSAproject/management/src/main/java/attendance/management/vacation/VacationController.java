package attendance.management.vacation;

import attendance.management.attendance.AttendanceService;
import attendance.management.sign.LoginUserDetails;
import attendance.management.utility.PageUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("vacation")
@Slf4j
public class VacationController {

    private final VacationService vacationService;

    @PostMapping("request")
    public ResponseEntity<VacationResponseDto> request(
            @RequestBody VacationReqDto vacationReqDto,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
            ) {
        if (loginUserDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        VacationResponseDto vacationResponseDto = vacationService.request(vacationReqDto, loginUserDetails);
        return ResponseEntity.ok(vacationResponseDto);
    }

    @GetMapping("view/{idx}")
    public ResponseEntity<VacationResponseDto> findOne(@PathVariable(name = "idx") long idx) {
        VacationResponseDto vacationResponseDto = vacationService.viewPage(idx);
        return ResponseEntity.ok(vacationResponseDto);
    }

    @GetMapping("/student")
    public ResponseEntity<VacationResponsePageDto> studentFindAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        if(loginUserDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        VacationResponsePageDto vacationResponsePageDto = vacationService.studentPage(PageUtil.getPageable(pageNum, size), loginUserDetails.getIdx());
        return ResponseEntity.ok(vacationResponsePageDto);
    }

    @GetMapping("/studentunchecked")
    public ResponseEntity<VacationResponsePageDto> studentUnchecked(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        if(loginUserDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        VacationResponsePageDto vacationResponsePageDto = vacationService.studentUnchecked(PageUtil.getPageable(pageNum, size), loginUserDetails.getIdx());
        return ResponseEntity.ok(vacationResponsePageDto);
    }

    @GetMapping("/teacher")
    public ResponseEntity<VacationResponsePageDto> teacherFindAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails){
        if(loginUserDetails==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        VacationResponsePageDto vacationResponsePageDto = vacationService.teacherPage(PageUtil.getPageable(pageNum, size), loginUserDetails);
        return ResponseEntity.ok(vacationResponsePageDto);
    }

    @GetMapping("/manager")
    public ResponseEntity<VacationResponsePageDto> managerFindAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size){
        VacationResponsePageDto vacationResponsePageDto = vacationService.managerPage(PageUtil.getPageable(pageNum, size));
        return ResponseEntity.ok(vacationResponsePageDto);
    }

    @GetMapping("/managersearch")
    public ResponseEntity<VacationResponsePageDto> managerSearch(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "name") String name
            ){
        VacationResponsePageDto vacationResponsePageDto = vacationService.managerSearch(PageUtil.getPageable(pageNum, size), name);
        return ResponseEntity.ok(vacationResponsePageDto);
    }

    @GetMapping("/managerunchecked")
    public ResponseEntity<VacationResponsePageDto> managerUnChecked(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        VacationResponsePageDto vacationResponsePageDto = vacationService.managerUnChecked(PageUtil.getPageable(pageNum, size));
        return ResponseEntity.ok(vacationResponsePageDto);
    }

    @PostMapping("/accept/{idx}")
    public ResponseEntity<String> accept(@PathVariable("idx") long idx) throws Exception {
        vacationService.accept(idx);
        String hwpName = vacationService.newHWP(idx);
        vacationService.savevacation(idx);
        return ResponseEntity.ok(hwpName);
    }

    @PostMapping("/deny/{idx}")
    public ResponseEntity<String> deny(@PathVariable("idx") long idx) {
        vacationService.deny(idx);
        return ResponseEntity.ok("휴가 요청 거부됨");
    }

    @GetMapping("/download/hwp/{idx}")
    public ResponseEntity<Resource> downloadHwpFile(@PathVariable("idx") long idx) throws IOException {

        VacationFileDto vacationFileDto = vacationService.download(idx);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/hwp"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(vacationFileDto.getFileName(), StandardCharsets.UTF_8) + "\"")
                .body(vacationFileDto.getResource());
    }

}
