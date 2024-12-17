package com.management.user_service.sign;

import com.management.user_service.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginReqDto {
    @NotEmpty
    @Schema(description = "아이디", example = "userid")
    private String userid;

    @NotEmpty
    @Schema(description = "비밀번호", example = "password")
    private String password;

    @NotEmpty
    @Schema(description = "권한", example = "ROLE_STUDENT")
    private Role role;

}
