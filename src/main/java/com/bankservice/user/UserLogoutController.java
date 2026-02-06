package com.bankservice.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserLogoutController {

    @PostMapping("/logout")
    public String logout(HttpSession session) {

        // ✅ 세션 완전 종료
        session.invalidate();

        // 로그인 화면으로 이동
        return "redirect:/login";
    }
}
