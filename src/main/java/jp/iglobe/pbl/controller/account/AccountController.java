package jp.iglobe.pbl.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.account.AccountForm;
import jp.iglobe.pbl.repository.AccountRepository;

@Controller
public class AccountController {
	@Autowired
	AccountRepository accountRepository;

	//	アカウント登録
	@GetMapping("/S0030")
	public String account(Model model) {
		model.addAttribute("accountForm", new AccountForm());
		return "S0030";
	}

	//	確認画面から戻って来る場合。値を保持させるがエラーメッセージは消える仕様
	@PostMapping("/S0030_back")
	public String backToS0030(@ModelAttribute AccountForm accountForm, Model model) {
		model.addAttribute("accountForm", accountForm);
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

	@PostMapping("/S0031_register")
	public String register(@ModelAttribute AccountForm accountForm) {

		// 1. Repositoryが扱う「Account」クラスに値をセット
		Account account = new Account();
		account.setName(accountForm.getName());
		account.setMail(accountForm.getMail());
		account.setPassword(accountForm.getPassword());
		account.setAuthority(accountForm.getAuthority());

		// 2. DBに保存実行！
		accountRepository.save(account);

		return "S0030";
	}

}
