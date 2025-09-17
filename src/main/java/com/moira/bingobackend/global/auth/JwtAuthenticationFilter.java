package com.moira.bingobackend.global.auth;

import com.moira.bingobackend.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final List<RequestMatcher> EXCLUDE_REQUEST_MATCHERS = List.of(
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/api/signup/**"),
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/signup/**"),
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/login/**")
    );

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return EXCLUDE_REQUEST_MATCHERS.stream().anyMatch(matcher -> matcher.matches(request));
    }

    private void sendErrorResponse(
            HttpServletResponse response,
            String message,
            ErrorCode errorCode,
            HttpStatus status
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String errorResponse = String.format(
                "{\"message\": \"%s\", \"errorCode\": \"%s\", \"status\": \"%s\", \"time\": \"%s\"}"
                , message, errorCode, status, LocalDateTime.now()
        );
        response.getWriter().write(errorResponse);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException {
        log.info("[요청 URI] {}", request.getRequestURI());

        // 1. Authorization 헤더 값 가져오기
        String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(BEARER_PREFIX)) {
            sendErrorResponse(
                    response,
                    "Authorization 헤더에 토큰 정보가 포함되어 있지 않습니다.",
                    ErrorCode.INVALID_AUTHORIZATION_HEADER,
                    HttpStatus.BAD_REQUEST
            );

            return;
        }

        // 2. AccessToken 추출
        String atk = jwtProvider.substringToken(authorizationHeaderValue);

        try {
            // 3. AccessToken에서 멤버 정보 추출
            Claims claims = jwtProvider.getUserInfoFromToken(atk);

            String userId = claims.getSubject();
            String email = claims.get("email", String.class);
            String role = claims.get("role", String.class);
            String nickname = claims.get("nickname", String.class);

            log.info("[접속 유저] {}: {}", nickname, email);

            SimpleUserAuth simpleUserAuth = new SimpleUserAuth(userId, email, role, nickname);

            // 4. Spring Security 권한 및 Authentication 객체 생성
            List<SimpleGrantedAuthority> authorities =
                    Stream.of(role)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(simpleUserAuth, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(
                    response,
                    "Access Token이 만료되었습니다.",
                    ErrorCode.EXPIRED_ATK,
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            sendErrorResponse(
                    response,
                    "유효하지 않은 토큰입니다.",
                    ErrorCode.INVALID_TOKEN,
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}