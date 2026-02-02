package com.bankservice.user;

import com.bankservice.auth.dto.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserViewController {

    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }
    // 중복확인
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Valid SignupRequest request, BindingResult bindingResult, Model model) {

        // 1️⃣ 아이디 중복 체크
        if (userService.existsByUserId(request.getUserId())) {
            bindingResult.rejectValue("userId", "error.userId", "이미 사용 중인 아이디입니다.");
        }

        // 2️⃣ 이메일 중복 체크
        if (userService.existsByEmail(request.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "이미 사용 중인 이메일입니다.");
        }

        // 3️⃣ 주민번호 중복 체크
        if (userService.existsByResidentNumber(request.getResidentNumber())) {
            bindingResult.rejectValue("residentNumber", "error.residentNumber", "이미 등록된 주민번호입니다.");
        }

        // 4️⃣ 다른 유효성 검사 에러가 있다면 다시 폼으로
        if (bindingResult.hasErrors()) {
            model.addAttribute("signupRequest", request);
            return "signup";
        }

        // 5️⃣ 모든 체크 통과 → 회원가입
        try {
            userService.signup(request);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("signupRequest", request);
            return "signup";
        }
    }
}
