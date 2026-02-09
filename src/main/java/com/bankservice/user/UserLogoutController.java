package com.bankservice.user;

import com.bankservice.token.RefreshTokenRedisRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserLogoutController {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public UserLogoutController(RefreshTokenRedisRepository refreshTokenRedisRepository) {
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    }

    @PostMapping("/logout")
    public void logout(Authentication authentication) {

        if (authentication != null) {
            String userId = authentication.getName();

            // ✅ Redis에서 Refresh Token 제거
            refreshTokenRedisRepository.deleteByUserId(userId);

            // ✅ 인증 정보 제거
            SecurityContextHolder.clearContext();
        }
    }
}
