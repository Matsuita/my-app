package jp.iglobe.pbl.controller.login;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.login.LoginForm;
import jp.iglobe.pbl.service.LoginService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // =========================
    // ログイン画面
    // =========================

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("loginForm",new LoginForm());
        return "C0010";
    }

    // =========================
    // ログイン処理
    // =========================

    @PostMapping("/login")
    public String login(
            @Valid
            @ModelAttribute
            LoginForm loginForm,BindingResult result,HttpSession session,Model model) {

        // 入力エラー
        if(result.hasErrors()) {
            return "C0010";
        }

        // ログイン認証
        Account account = loginService.login(loginForm.getMail(),loginForm.getPassword());

        // 成功
        if(account != null) {
            session.setAttribute("loginUser",account);
            return "redirect:/dashboard";
        }

        // 失敗
        model.addAttribute("errorMessage","メールアドレスまたはパスワードが違います。");
        return "C0010";
    }

    // =========================
    // ログアウト
    // =========================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}