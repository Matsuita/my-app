package jp.iglobe.pbl.controller.sales;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.account.AccountDeleteForm;
import jp.iglobe.pbl.model.account.AccountSearchForm;
import jp.iglobe.pbl.model.account.AccountUpdateForm;
import jp.iglobe.pbl.repository.SearchRepository;

@Controller
@SessionAttributes({ "accountSearchForm", "accountUpdateForm" })
public class SearchController {

	@ModelAttribute("accountSearchForm")
	public AccountSearchForm setUpForm() {
		return new AccountSearchForm();
	}

	@Autowired
	private SearchRepository searchRepository;

	@GetMapping("/accounts/search")
	public String init(HttpSession session, Model model) {

		// アカウント権限「閲覧のみ」「登録・編集」（accountsAuthorityが1か2）にならない人を弾く
		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 1) {
			return "redirect:/dashboard";
		}
		model.addAttribute("accountSearchForm", new AccountSearchForm());
		return "S0040";
	}

	@PostMapping("/accounts/result") // 検索結果
	public String search(@Valid @ModelAttribute AccountSearchForm form,
			BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("accountSearchForm", form);
			return "S0040"; // 入力画面に戻る
		}
		List<Account> list = searchRepository.search(
				form.getName(),
				form.getMail(),
				form.getSalesAuthority(),
				form.getAccountsAuthority());

		model.addAttribute("accounts", list);
		model.addAttribute("accountSearchForm", form);
		return "S0041";
	}

	@GetMapping("/accounts/edit/{id}")
	public String edit(@PathVariable Integer id, HttpSession session, Model model) {

		// アカウント権限「登録・編集」（accountsAuthorityが1か2）にならない人を弾く
		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 2) {
			return "redirect:/dashboard";
		}

		AccountUpdateForm sessionForm = (AccountUpdateForm) model.getAttribute("accountUpdateForm");

		// 🔥 同じIDのときだけ使う
		if (sessionForm != null && id.equals(sessionForm.getAccountId())) {
			return "S0042";
		}

		// それ以外はDBから再取得
		Account account = searchRepository.findById(id).orElseThrow();

		AccountUpdateForm form = new AccountUpdateForm();
		form.setAccountId(account.getAccountId());
		form.setName(account.getName());
		form.setMail(account.getMail());
		form.setSalesAuthority(account.getSalesAuthority());
		form.setAccountsAuthority(account.getAccountsAuthority());

		model.addAttribute("accountUpdateForm", form);

		return "S0042";
	}

	@PostMapping("/accounts/confirm")
	public String confirm(
			@Valid @ModelAttribute AccountUpdateForm form,
			BindingResult result,
			Model model) {
		if (!result.hasFieldErrors("password")
				&& !result.hasFieldErrors("passwordConfirm")) {

			if (!form.getPassword().equals(form.getPasswordConfirm())) {
				result.rejectValue("passwordConfirm", null, "パスワードが一致していません。");
			}
		}
		if (result.hasErrors()) {
			return "S0042"; // 入力画面に戻す
		}
		// 🚨 ガード（直打ち対策）
		if (form.getAccountId() == null) {
			return "redirect:/accounts/search";
		}
		model.addAttribute("accountUpdateForm", form);
		return "S0043"; // 確認画面
	}

	@PostMapping("/accounts/update")
	public String update(@ModelAttribute AccountUpdateForm account, SessionStatus sessionStatus) {

		Account existing = searchRepository
				.findById(account.getAccountId())
				.orElseThrow();

		existing.setName(account.getName());
		existing.setMail(account.getMail());
		existing.setSalesAuthority(account.getSalesAuthority());
		existing.setAccountsAuthority(account.getAccountsAuthority());
		existing.setPassword(account.getPassword());

		searchRepository.save(existing);
		sessionStatus.setComplete(); // 🔥 これ必須

		return "redirect:/accounts/result";
	}

	// 確認画面
	@PostMapping("/accounts/delete")
	public String deleteConfirm(AccountDeleteForm form, Model model) {

		Account account = searchRepository
				.findById(form.getAccountId())
				.orElseThrow();

		model.addAttribute("account", account);

		return "S0044";
	}

	// 実際の削除 (←物理削除なので変更します。5月20日)
	//	@PostMapping("/accounts/delete/execute")
	//	public String deleteExecute(@ModelAttribute AccountDeleteForm account, SessionStatus sessionStatus) {
	//		searchRepository.deleteById(account.getAccountId());
	//		sessionStatus.setComplete();
	//		return "redirect:/accounts/result";
	//	}

	//	アカウント論理削除
	@PostMapping("/accounts/delete/execute")
	public String deleteExecute(@ModelAttribute AccountDeleteForm account, SessionStatus sessionStatus) {
		// 修正前：searchRepository.deleteById(account.getAccountId());
		// 修正後：新しく作った論理削除のメソッドを呼び出す
		searchRepository.logicalDeleteById(account.getAccountId());

		sessionStatus.setComplete();
		return "redirect:/accounts/result";
	}

	@GetMapping("/accounts/result")
	public String resultFromSession(
			HttpSession session,
			@ModelAttribute AccountSearchForm form,
			Model model,
			SessionStatus sessionStatus // ←追加
	) {
		// アカウント権限「閲覧のみ」「登録・編集」（accountsAuthorityが2）にならない人を弾く
		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 1) {
			return "redirect:/dashboard";
		}

		sessionStatus.setComplete(); // 🔥ここで編集内容リセット

		List<Account> list = searchRepository.search(
				form.getName(),
				form.getMail(),
				form.getSalesAuthority(),
				form.getAccountsAuthority());

		model.addAttribute("accounts", list);

		return "S0041";
	}

	@GetMapping("/accounts/delete")
	public String deleteConfirmGet(HttpSession session, AccountDeleteForm form, Model model) {

		// アカウント権限「登録・編集」（accountsAuthorityが2）にならない人を弾く
		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 2) {
			return "redirect:/dashboard";
		}

		// ガード
		if (form.getAccountId() == null) {
			return "redirect:/accounts/search";
		}

		Account account = searchRepository
				.findById(form.getAccountId())
				.orElse(null);

		if (account == null) {
			return "redirect:/accounts/search";
		}

		model.addAttribute("account", account);

		return "S0044";
	}

	@GetMapping("/accounts/confirm")
	public String confirmGet() {
		return "redirect:/accounts/search";
	}

	@PostMapping("/accounts/edit/back")
	public String back(SessionStatus sessionStatus) {

		sessionStatus.setComplete(); // ←これ追加🔥

		return "redirect:S0042";
	}

}
