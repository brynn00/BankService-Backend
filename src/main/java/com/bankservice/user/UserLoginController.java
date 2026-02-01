package com.bankservice.user;

import com.bankservice.auth.AuthService;
import com.bankservice.auth.dto.LoginRequest;
import com.bankservice.auth.dto.LoginResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserLoginController {

    private final AuthService authService;

    public UserLoginController(AuthService authService) {
        this.authService = authService;
    }

    // 로그인 화면
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        try {
            LoginResponse response = authService.login(
                    new LoginRequest(email, password)
            );

            // JWT 화면에 표시 (테스트용)
            model.addAttribute("accessToken", response.getAccessToken());
            model.addAttribute("refreshToken", response.getRefreshToken());
            model.addAttribute("expiresIn", response.getExpiresIn());

            return "login-success"; // 로그인 성공 화면
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login"; // 로그인 실패하면 다시 화면
        }
    }
}
