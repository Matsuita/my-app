package jp.iglobe.pbl.controller.dashboard;

import org.springframework.web.bind.annotation.GetMapping;

public class DashboardController {
	@GetMapping("/dashboard")
	public String dashboard() {

	    return "C0020";
	    
	}
	
}
