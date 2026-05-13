package jp.iglobe.pbl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.SalesSearchForm;

@Controller
public class SalesController {

    @GetMapping("/sales/search")
    public String search(Model model) {

        model.addAttribute("salesSearchForm", new SalesSearchForm());
        return "S0020";
    }

    @PostMapping("/sales/search")
    public String searchResult(Model model) {

        model.addAttribute("salesSearchForm", new SalesSearchForm());
        return "S0020";
    }
}