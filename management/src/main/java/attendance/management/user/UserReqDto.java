package attendance.management.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserReqDto {

    private Long idx;

    private String userid;

    private String password;

    private String name;

    private String phoneNumber;

    private String email;

    private String role;

}
