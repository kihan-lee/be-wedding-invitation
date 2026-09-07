package com.wedding.invitation.common.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.invitation.common.exception.ErrorCode;
import com.wedding.invitation.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증 실패 응답을 만듭니다.
 * JwtAuthenticationFilter가 요청 속성에 실패 사유를 담아두면 그대로 사용하고,
 * 없으면(토큰 자체가 오지 않은 경우) UNAUTHORIZED로 응답합니다. 덕분에
 * 클라이언트가 만료(AUTH_003)와 위조(AUTH_002), 미인증(AUTH_001)을 구분
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        ErrorCode errorCode = resolveErrorCode(request);

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> body = ApiResponse.error(errorCode.getCode(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private ErrorCode resolveErrorCode(HttpServletRequest request) {
        Object attribute = request.getAttribute(JwtAuthenticationFilter.AUTH_ERROR_CODE);
        if (attribute instanceof ErrorCode errorCode) {
            return errorCode;
        }
        return ErrorCode.UNAUTHORIZED;
    }
}