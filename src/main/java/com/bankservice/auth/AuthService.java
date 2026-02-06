package com.bankservice.auth;

import com.bankservice.auth.dto.LoginRequest;
import com.bankservice.auth.dto.LoginResponse;
import com.bankservice.token.RefreshToken;
import com.bankservice.token.RefreshTokenRepository;
import com.bankservice.user.User;
import com.bankservice.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            BCryptPasswordEncoder encoder,
            JwtProvider jwtProvider
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
    }


    public LoginResponse login(LoginRequest request) {

        // 1. 사용자 조회
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // 2. 상태 검증
        if (!user.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("차단된 사용자");
        }

        // 3. 비밀번호 검증
        if (!encoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        // 4. 토큰 생성
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken();

        // 5. Refresh Token 저장 (1인 1토큰)
        RefreshToken token = refreshTokenRepository
                .findByUser(user)
                .orElse(new RefreshToken());

        token.setUser(user);
        token.setToken(refreshToken);
        token.setExpiredAt(LocalDateTime.now().plusDays(14));

        refreshTokenRepository.save(token);

        return new LoginResponse(accessToken, refreshToken, 1800);
    }
    private String encrypt(String value) {
        return value;
    }
}
