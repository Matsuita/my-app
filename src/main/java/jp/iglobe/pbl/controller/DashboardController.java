package jp.iglobe.pbl.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class DashboardController {
	@GetMapping("/C0020")
	public String dashboard() {

	    return "C0020";
	}
	
}
