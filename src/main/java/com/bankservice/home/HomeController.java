package com.bankservice.home;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        String userName = (String) session.getAttribute("userName");
        String accessToken = (String) session.getAttribute("accessToken");

        model.addAttribute("userName", userName);
        model.addAttribute("accessToken", accessToken);
        model.addAttribute("expiresIn", 10); // 🔥 로그인 정보


        return "home";
    }
}
