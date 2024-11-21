package attendance.management.announce;

import lombok.Data;

@Data
public class AnnounceResponseDto {

    private Long idx;
    private String title;
    private String body;
    private String wdate;
    private String user;
    private String lecture;
}
