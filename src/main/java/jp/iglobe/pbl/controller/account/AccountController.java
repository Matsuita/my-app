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
import jp.iglobe.pbl.service.AccountService;

@Controller
public class AccountController {
	@Autowired
	AccountService accountService;

	//	アカウント登録
	@GetMapping("/accounts")
	public String account(HttpSession session, Model model) {

		// アカウント権限「登録・編集」（accountsAuthorityが2）にならない人を弾く
		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser == null || loginUser.getAccountsAuthority() < 2) {
			return "redirect:/dashboard";
		}
		// 登録用のフォームを作りServiceに投げる
		AccountForm form = accountService.getNewAccountForm();
		model.addAttribute("accountForm", form);
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
	public String showConfirm(HttpSession session, Model model) {
		
		// アカウント権限「登録・編集」（accountsAuthorityが2）にならない人を弾く
		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser == null || loginUser.getAccountsAuthority() < 2) {
			return "redirect:/dashboard";
		}

		model.addAttribute("accountForm", new AccountForm());
		return "redirect:/accounts";
	}

	//	アカウント登録確認
	@PostMapping("/accounts/confirm")
	public String confirmRegister(@Validated @ModelAttribute AccountForm accountForm, BindingResult result) {

		// Serviceにパスワード一致やメール重複などのチェックを実行
		// エラーが見つかっていたら、入力画面（S0030）に戻す
		accountService.validateAccountForm(accountForm, result);
		if (result.hasErrors()) {
			return "S0030";
		}

		// 全てOKなら確認画面（S0031）へ
		return "S0031";

	}

	@PostMapping("/accounts/confirm_register")
	public String register(@ModelAttribute AccountForm accountForm) {

		// ServiceにDB保存処理
		accountService.registerAccount(accountForm);

		// 保存が終わったら、一覧画面（/accounts）へ
		return "redirect:/accounts";
	}

}
