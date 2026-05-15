package jp.iglobe.pbl.controller;

import java.util.List;

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

import jp.iglobe.pbl.model.AccountDeleteForm;
import jp.iglobe.pbl.model.AccountForm;
import jp.iglobe.pbl.model.AccountSearchForm;
import jp.iglobe.pbl.model.SearchAccount;
import jp.iglobe.pbl.repository.SearchRepository;

@Controller
@SessionAttributes("accountSearchForm")
public class SearchController {

	@Autowired
	private SearchRepository searchRepository;

	@GetMapping("/accounts/search")
	public String init(Model model) {
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
		List<SearchAccount> list = searchRepository.search(
				form.getName(),
				form.getMail(),
				form.getAuthority());

		model.addAttribute("accounts", list);
		model.addAttribute("accountSearchForm", form);
		return "S0041";
	}

	@GetMapping("/accounts/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {

		SearchAccount account = searchRepository.findById(id).orElse(null);
		model.addAttribute("account", account);

		return "S0042";
	}

	@PostMapping("/accounts/confirm")
	public String confirm(AccountForm account, Model model) {
		model.addAttribute("account", account);
		return "S0043"; // 遷移先HTML
	}

//	@PostMapping("/accounts/update")
//	public String update(@ModelAttribute AccountForm account) {
//
//		SearchAccount accounts = new SearchAccount();
//
//		accounts.setAccountId(account.getAccountId());
//		accounts.setName(account.getName());
//		accounts.setMail(account.getMail());
//		accounts.setAuthority(account.getAuthority());
//		accounts.setPassword(account.getPassword());
//
//		searchRepository.save(accounts);
//
//		return "redirect:/accounts/search";
//	}

	// 確認画面
	@PostMapping("/accounts/delete")
	public String deleteConfirm(AccountDeleteForm account, Model model) {
		model.addAttribute("account", account);
		return "S0044";
	}

	// 実際の削除
	@PostMapping("/accounts/delete/execute")
	public String deleteExecute(@ModelAttribute AccountDeleteForm account) {
		searchRepository.deleteById(account.getAccountId());
		return "redirect:/accounts/result";
	}

	@GetMapping("/accounts/result")
	public String resultFromSession(
			@ModelAttribute("accountSearchForm") AccountSearchForm form,
			Model model) {

		List<SearchAccount> list = searchRepository.search(
				form.getName(),
				form.getMail(),
				form.getAuthority());

		model.addAttribute("accounts", list);

		return "S0041";
	}
}
