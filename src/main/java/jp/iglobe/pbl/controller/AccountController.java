package jp.iglobe.pbl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String confirm(@ModelAttribute AccountForm accountForm) {
		return "S0031";
	}
}
