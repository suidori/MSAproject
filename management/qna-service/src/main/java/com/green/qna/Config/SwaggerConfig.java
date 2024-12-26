
package com.green.qna.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "QnA-Service API 명세서",
                description = "오늘 오징어게임2 나옴",
                version = "v112"))
@Configuration
public class SwaggerConfig {
}