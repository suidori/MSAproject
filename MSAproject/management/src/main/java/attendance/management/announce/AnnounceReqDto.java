package attendance.management.announce;

import lombok.Data;

@Data
public class AnnounceReqDto {

    private String title;

    private String body;

    private Long lecture;

}
