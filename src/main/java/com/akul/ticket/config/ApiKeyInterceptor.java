package com.akul.ticket.config;

import com.akul.ticket.exception.UnauthorizedException;
import com.akul.ticket.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String[] SECURED_PATHS = {
            "/bookings"
    };

    private final UserService userService;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        var path = request.getRequestURI();

        for (var securedPath : SECURED_PATHS) {
            if (path.startsWith(securedPath)) {
                return validateUser(request);
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean validateUser(@NonNull HttpServletRequest request) {
        var auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        var user = userService.getByUsername(auth);
        if (user.isEmpty()) {
            throw new UnauthorizedException("missing or invalid username, please put in header Authorization: <username>");
        }
        return true;
    }
}
