package com.bankservice.user;

import com.bankservice.auth.AuthService;
import com.bankservice.auth.dto.LoginRequest;
import com.bankservice.auth.dto.LoginResponse;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/login")
    public String login(
            @RequestParam String userId,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        try {
            LoginResponse response = authService.login(
                    new LoginRequest(userId, password)
            );

            // ✅ 세션에 로그인 정보 저장
            session.setAttribute("userName", response.getUserName());
            session.setAttribute("accessToken", response.getAccessToken());
            session.setAttribute("expiresIn", response.getAccessExpiresIn());

            return "redirect:/home"; // 리다이렉트 권장
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }



    // 로그인 처리 -- AccessToken, RefreshToken 확인용
    /*@PostMapping("/login")
    public String login(
            @RequestParam String userId,
            @RequestParam String password,
            Model model
    ) {
        try {
            LoginResponse response = authService.login(
                    new LoginRequest(userId, password)
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
    }*/


}
