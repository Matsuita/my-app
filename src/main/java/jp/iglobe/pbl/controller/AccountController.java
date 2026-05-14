package jp.iglobe.pbl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.AccountForm;

@Controller
public class AccountController {

	//	アカウント登録
	@GetMapping("/S0030")
	public String account(Model model) {
		model.addAttribute("accountForm", new AccountForm());
		return "S0030";
	}

	//	アカウント登録確認
	@PostMapping("/S0031")
	public String confirm(@Validated @ModelAttribute AccountForm accountForm,
			BindingResult result) {

		// 1. 入力チェック（@NotBlankや@NotNull）に引っかかった場合
		if (result.hasErrors()) {
			return "S0030";
		}

		// 2. パスワード一致チェック（カスタムチェック）
		if (!accountForm.getPassword().equals(accountForm.getPasswordConfirm())) {
			// passwordConfirmフィールドに対して個別にエラーを紐付ける
			result.rejectValue("passwordConfirm", "error.passwordConfirm", "パスワードが一致しません");
			return "S0030"; // 入力画面へ戻る
		}
		int totalAuth = 0;
		if (accountForm.getAuthList() != null) {
			for (Integer val : accountForm.getAuthList()) {
				totalAuth += val;
			}
		}
		// ここで 0, 1, 2, 3 のいずれかになる
		accountForm.setAuthority(totalAuth);

		// 全てOKなら確認画面（S0031）へ
		return "S0031";

	}

}
