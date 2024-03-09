package com.akul.ticket.config;

import com.akul.ticket.service.UserService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SecurityScheme(
        name = "api-key",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization",
        description = "please put username"
)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    /*
      FOR THE RECORD!
      this is not the best way for request security
      it's just simple example for test simplicity

      if you want  a proper request security (jwt, api-key, oauth)
      then use spring-boot-starter-security
      such as @EnableWebSecurity
     */

    private final UserService userService;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new ApiKeyInterceptor(userService));
    }
}
