package attendance.management.vacation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationResponsePageDto {

    @JsonIgnore
    private List<Vacation> content;

    private List<VacationResponseDto> list;
    private long totalElements;
    private int totalPages;
    private int size;
}
