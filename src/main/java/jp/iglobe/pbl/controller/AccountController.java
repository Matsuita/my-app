package jp.iglobe.pbl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.iglobe.pbl.model.AccountForm;

@Controller
public class AccountController {

@GetMapping("/S0030")
    public String account(Model model) {
	
        model.addAttribute("accountForm",  new AccountForm());
        return "S0030";
    }
}
