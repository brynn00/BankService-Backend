package com.bankservice.auth;

import com.bankservice.auth.dto.LoginRequest;
import com.bankservice.auth.dto.LoginResponse;
import com.bankservice.auth.dto.TokenRefreshRequest;
import com.bankservice.auth.dto.TokenRefreshResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ✅ 로그인
     * 클라이언트 타이머용 expiresIn 초 전달
     */
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request,
            HttpServletResponse response,
            HttpSession session

    ) {
        LoginResponse loginResponse = authService.login(request);
        int ttlSeconds = authService.getRemainingSeconds(loginResponse.getRefreshToken());

        // userName 쿠키 URL Encode (한글 안전 처리)
        String encodedUserName = URLEncoder.encode(loginResponse.getUserName(), StandardCharsets.UTF_8);
        Cookie userNameCookie = new Cookie("userName", encodedUserName);
        userNameCookie.setPath("/");
        userNameCookie.setHttpOnly(false);
        userNameCookie.setMaxAge(ttlSeconds);
        response.addCookie(userNameCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(ttlSeconds);
        response.addCookie(refreshTokenCookie);

        // 클라이언트 JS용 expiresIn 계산 (초 단위)
        int expiresIn = authService.getRemainingSecondsByRefreshToken(loginResponse.getRefreshToken());
        // ✅ 세션에도 저장
        session.setAttribute("userName", loginResponse.getUserName());
        session.setAttribute("expiresIn", expiresIn);

        return loginResponse;
    }

    /**
     * ✅ AccessToken 재발급
     */
    @PostMapping("/refresh")
    public TokenRefreshResponse refresh(
            @RequestBody TokenRefreshRequest request
    ) {
        return authService.refresh(request.getRefreshToken());
    }


    /**
     * ✅ 로그아웃
     */
    @PostMapping("/logout")
    public void logout(
            @RequestBody(required = false) TokenRefreshRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        if (request != null && request.getRefreshToken() != null) {
            authService.logout(request.getRefreshToken());
        }

        // 세션 초기화
        session.invalidate();

        // 쿠키 삭제
        Cookie userNameCookie = new Cookie("userName", null);
        userNameCookie.setPath("/");
        userNameCookie.setMaxAge(0);
        response.addCookie(userNameCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }

    @GetMapping("/token/ttl")
    public int checkTokenTtl(@RequestParam String refreshToken) {
        return authService.getRemainingSeconds(refreshToken);
    }
}
