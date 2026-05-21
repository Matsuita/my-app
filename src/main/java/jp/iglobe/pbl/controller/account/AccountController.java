package jp.iglobe.pbl.controller.account;

import jakarta.servlet.http.HttpSession;

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
	@GetMapping("/accounts")
	public String account(HttpSession session, Model model) {

		// アカウント権限「登録・編集」（accountsAuthorityが2）にならない人を弾く
		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 2) {
			return "redirect:/dashboard";
		}

		model.addAttribute("accountForm", new AccountForm());
		return "S0030";
	}

	//	確認画面から戻って来る場合。値を保持させるがエラーメッセージは消える仕様
	@PostMapping("/accounts_back")
	public String backToS0030(@ModelAttribute AccountForm accountForm, Model model) {
		model.addAttribute("accountForm", accountForm);
		return "S0030";
	}

	//	アカウント登録確認画面へ直接は飛ばず、アカウント登録画面へ遷移
	@GetMapping("/accounts/confirm")
	public String confirm(HttpSession session, Model model) {
		// アカウント権限「登録・編集」（accountsAuthorityが2）にならない人を弾く
		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 2) {
			return "redirect:/dashboard";
		}

		model.addAttribute("accountForm", new AccountForm());
		return "redirect:/accounts";
	}

	//	アカウント登録確認
	@PostMapping("/accounts/confirm")
	public String confirm(@Validated @ModelAttribute AccountForm accountForm,
			BindingResult result) {

		// 1. 入力チェック（@NotBlankや@NotNull）に引っかかった場合
		if (result.hasErrors()) {
			return "S0030";
		}

		// 2. パスワード一致チェック（カスタムチェック）
		if (!accountForm.getPassword().equals(accountForm.getPasswordConfirm())) {
			// passwordConfirmフィールドに対して個別にエラーを紐付ける
			result.rejectValue("passwordConfirm", "error.passwordConfirm", "パスワードとパスワード（確認）の入力値が異なります。");
			return "S0030"; // 入力画面へ戻る
		}

		// データベースに同じメールアドレスがあるか直接チェックする
		if (accountRepository.existsByMail(accountForm.getMail())) {
			result.rejectValue("mail", "error.mail", "このメールアドレスは既に使用されているため、別のパスワードで登録してください。");
			return "S0030"; // 重複していたら入力画面（S0030）へ戻る
		}

		// 全てOKなら確認画面（S0031）へ
		return "S0031";

	}

	@PostMapping("/accounts/confirm_register")
	public String register(@ModelAttribute AccountForm accountForm) {

		// Repositoryが扱う「Account」クラスに値をセット
		//（authorityはこのメソッドで生成）
		Account account = new Account();
		account.setName(accountForm.getName());
		account.setMail(accountForm.getMail());
		account.setPassword(accountForm.getPassword());
		account.setSalesAuthority(accountForm.getSalesAuthority());
		account.setAccountsAuthority(accountForm.getAccountsAuthority());

		// DB保存を実行
		account.setActive(true);
		accountRepository.save(account);

		return "redirect:/accounts";
	}

}
