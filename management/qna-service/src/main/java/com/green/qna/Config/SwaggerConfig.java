
package com.green.qna.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "QnA-Service API 명세서",
                description = "ㅇㅇ",
                version = "v112"))
@Configuration
public class SwaggerConfig {
}