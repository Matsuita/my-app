package jp.iglobe.pbl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.Sales;

@Controller
public class SalesController {
	@GetMapping("/sales")
	public String newSale(Model model) {
		model.addAttribute("sale", new Sales());
		return "S0010";
	}
	
	@PostMapping("/sales/form-confirm")
	public String salesConfirm(Model model) {
		model.addAttribute("sale", new Sales());
		return "S0011";
	}
	


 }

