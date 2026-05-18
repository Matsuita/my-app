package jp.iglobe.pbl.controller.sales;

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

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.account.AccountDeleteForm;
import jp.iglobe.pbl.model.account.AccountSearchForm;
import jp.iglobe.pbl.model.account.AccountUpdateForm;
import jp.iglobe.pbl.repository.SearchRepository;

@Controller

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
		List<Account> list = searchRepository.search(
				form.getName(),
				form.getMail(),
				form.getAuthority());

		model.addAttribute("accounts", list);
		model.addAttribute("accountSearchForm", form);
		return "S0041";
	}

	@GetMapping("/accounts/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {

		Account account = searchRepository.findById(id).orElse(null);
		model.addAttribute("accountUpdateForm", account);

		return "S0042";
	}

	@PostMapping("/accounts/confirm")
	public String confirm(
			@Valid @ModelAttribute AccountUpdateForm form,
	        BindingResult result,
	        Model model) {

	    if (result.hasErrors()) {
	        return "S0042"; // 入力画面に戻す
	    }
	    model.addAttribute("accountUpdateForm", form);
	    return "S0043"; // 確認画面
	}
	@PostMapping("/accounts/update")
	public String update(@ModelAttribute AccountUpdateForm account) {

	    Account existing = searchRepository
	            .findById(account.getAccountId())
	            .orElseThrow();

	    existing.setName(account.getName());
	    existing.setMail(account.getMail());
	    existing.setAuthority(account.getAuthority());
	    existing.setPassword(account.getPassword());

	    searchRepository.save(existing);

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

	// 実際の削除
	@PostMapping("/accounts/delete/execute")
	public String deleteExecute(@ModelAttribute AccountDeleteForm account) {
		searchRepository.deleteById(account.getAccountId());
		return "redirect:/accounts/result";
	}

	@GetMapping("/accounts/result")
	public String resultFromSession(
			@ModelAttribute AccountSearchForm form,
			Model model) {
		
		
		List<Account> list = searchRepository.search(
				form.getName(),
				form.getMail(),
				form.getAuthority());

		model.addAttribute("accounts", list);

		return "S0041";
	}
}
