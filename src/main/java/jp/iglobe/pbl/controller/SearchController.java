package jp.iglobe.pbl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jp.iglobe.pbl.model.AccountSearchForm;
import jp.iglobe.pbl.model.SearchAccount;
import jp.iglobe.pbl.repository.AccountRepository;

@Controller
public class SearchController  {

	@Autowired
	private AccountRepository accountRepository;
	@GetMapping("/accounts/search")      // 入力画面
	public String init() {
	    return "S0040";
	}

	@GetMapping("/accounts/result")      // 検索結果
	public String search(AccountSearchForm form, Model model) {
	    List<SearchAccount> list = accountRepository.search(
	            form.getName(),
	            form.getMail(),
	            form.getAuthority());

	    model.addAttribute("accounts", list);
	    return "S0041";
	}
	
	@GetMapping("/accounts/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {

	    SearchAccount account = accountRepository.findById(id).orElse(null);
	    model.addAttribute("account", account);

	    return "S0042";
	}
	
	
	
	
}