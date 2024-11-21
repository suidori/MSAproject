package attendance.management.announce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnounceResponsePageDto {

    @JsonIgnore
    private List<Announce> content;

    private List<AnnounceResponseDto> list;
    private long totalElements;
    private int totalPages;
    private int size;
}
