package jp.iglobe.pbl.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.LoginForm;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("loginForm", new LoginForm());

        return "C0010";
    }

    @PostMapping("/login")
    public String login(
    @Valid
    @ModelAttribute LoginForm loginForm, BindingResult result, Model model) {

        // 入力チェック
        if (result.hasErrors()) {
            return "C0010";
        }

        if (loginForm.getMail().equals("admin@test.com") && loginForm.getPassword().equals("1234")) {
            return "redirect:/dashboard";
        }

        model.addAttribute("errorMessage", "メールアドレスまたはパスワードが違います。");
        return "C0010";
    }
}