package com.interview.task.security;

import com.interview.task.enums.Message;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class represents handler for unauthorized users.
 */
@Component
public class JwtEntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    /**
     * Method handle error response.
     *
     * @param request request
     * @param response response
     * @param authException authentication exception
     * @throws IOException IOException
     * @throws ServletException ServletException
     */
    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Message.UNAUTHORIZED_ACCESS.getMsgBody());
    }
}
