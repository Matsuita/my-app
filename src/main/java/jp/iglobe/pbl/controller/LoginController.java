package jp.iglobe.pbl.controller;

import jakarta.servlet.http.HttpSession;
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

    // =========================
    // ログイン画面
    // =========================

    @GetMapping("/")
    public String index(
            HttpSession session,
            Model model) {

        // ログアウト状態
        session.invalidate();

        model.addAttribute(
                "loginForm",
                new LoginForm());

        return "C0010";
    }

    // =========================
    // ログイン処理
    // =========================

    @PostMapping("/login")
    public String login(

            @Valid
            @ModelAttribute
            LoginForm loginForm,

            BindingResult result,

            HttpSession session,

            Model model) {

        // 入力エラー
        if (result.hasErrors()) {

            return "C0010";
        }

        // メール検索
        Account account =
                accountRepository.findByMail(
                        loginForm.getMail());

        // ログイン成功
        if (account != null) {

            if (account.getPassword()
                    .equals(
                        loginForm.getPassword())) {

                // Session保存
                session.setAttribute(
                        "loginUser",
                        account);

                return "redirect:/dashboard";
            }
        }

        // ログイン失敗
        model.addAttribute(
                "errorMessage",
                "メールアドレスまたはパスワードが違います。");

        return "C0010";
    }

    // =========================
    // ダッシュボード
    // =========================

    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession session) {

        // 未ログイン
        if (session.getAttribute("loginUser")
                == null) {

            return "redirect:/";
        }

        return "C0020";
    }

    // =========================
    // ログアウト
    // =========================

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        // Session削除
        session.invalidate();

        return "redirect:/";
    }
}