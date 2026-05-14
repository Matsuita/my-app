package jp.iglobe.pbl.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class DashboardController {
	@GetMapping("/dashboard")
	public String dashboard() {

	    return "C0020";
	    
	}
	
}
