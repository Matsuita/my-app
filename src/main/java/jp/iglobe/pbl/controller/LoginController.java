package jp.iglobe.pbl.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.Account;
import jp.iglobe.pbl.model.LoginForm;
import jp.iglobe.pbl.repository.AccountRepository;

@Controller
public class LoginController {

    @Autowired
    private AccountRepository accountRepository;

    // ログイン画面
    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("loginForm", new LoginForm());
        return "C0010";
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(
    @Valid
    @ModelAttribute LoginForm loginForm, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "C0010";
        }

        Account account = accountRepository.findByMail(loginForm.getMail());

        if (account != null) {
            if (account.getPassword().equals(loginForm.getPassword())) {
                return "redirect:/dashboard";
            }
        }

        model.addAttribute("errorMessage", "メールアドレスまたはパスワードが違います。");

        return "C0010";
    }

    // ダッシュボード
    @GetMapping("/dashboard")
    public String dashboard() {

        return "C0020";
    }
}