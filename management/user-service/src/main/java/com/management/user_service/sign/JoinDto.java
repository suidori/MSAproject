package com.management.user_service.sign;

import com.management.user_service.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JoinDto {

    @NotEmpty
    @Schema(description = "아이디", example = "userid")
    private String userid;

    @NotEmpty
    @Schema(description = "비밀번호", example = "password")
    private String password;

    @NotEmpty
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotEmpty
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @NotEmpty
    @Schema(description = "권한", example = "ROLE_STUDENT")
    private Role role;

    @Schema(description = "이메일", example = "aaa@example.com")
    private String email;

}
