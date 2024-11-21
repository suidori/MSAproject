package attendance.management.userandlecture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndLectureResponsePageDto {

    @JsonIgnore
    private List<UserAndLecture> content;

    private List<UserAndLectureResponseDto> list;
    private long totalElements;
    private int totalPages;
    private int size;
}
