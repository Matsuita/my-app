package jp.iglobe.pbl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.Sales;
import jp.iglobe.pbl.model.SalesSearchForm;
import jp.iglobe.pbl.repository.SalesRepository;

@Controller
public class SalesController {

    @Autowired
    private SalesRepository salesRepository;

    // 画面表示
    @GetMapping("/S0020")
    public String search(Model model) {

        model.addAttribute(
                "salesSearchForm",
                new SalesSearchForm());

        return "S0020";
    }

    // 検索
    @PostMapping("/sales/search")
    public String searchResult(
            @ModelAttribute
            SalesSearchForm salesSearchForm,
            Model model) {

        // 商品名検索
        List<Sales> salesList =
                salesRepository.findByTradeNameContaining(
                        salesSearchForm.getTradeName());
        model.addAttribute(
                "salesList",
                salesList);

        return "S0020";
    }
}
