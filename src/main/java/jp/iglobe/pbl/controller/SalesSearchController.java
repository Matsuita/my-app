package jp.iglobe.pbl.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.Sale;
import jp.iglobe.pbl.model.SalesForm;
import jp.iglobe.pbl.model.SalesSearchForm;
import jp.iglobe.pbl.repository.AccountRepository;
import jp.iglobe.pbl.repository.CategoryRepository;
import jp.iglobe.pbl.repository.SalesRepository;

@Controller
public class SalesSearchController {

    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;

 // 検索画面
    @GetMapping("/S0020")
    public String search(Model model) {

        model.addAttribute(
                "salesSearchForm",
                new SalesSearchForm());

        // 担当一覧
        model.addAttribute(
                "accountList",
                accountRepository.findAll());

        // カテゴリー一覧
        model.addAttribute(
                "categoryList",
                categoryRepository.findAll());

        return "S0020";
    }
    

 // 検索処理
    @PostMapping("/sales/search")
    public String searchResult(
            @ModelAttribute
            SalesSearchForm salesSearchForm,
            Model model) {

        List<Sale> salesList =
            salesRepository.search(

                salesSearchForm.getSaleDateFrom(),

                salesSearchForm.getSaleDateTo(),

                salesSearchForm.getAccountId(),

                salesSearchForm.getCategoryId(),

                salesSearchForm.getTradeName(),

                salesSearchForm.getNote()
            );

        model.addAttribute(
                "salesList",
                salesList);

        model.addAttribute(
                "salesSearchForm",
                salesSearchForm);

        return "S0021";
    }

    // 詳細画面
    @GetMapping("/sales/detail")
    public String detail(Integer saleId, Model model) {

        Sale sales = salesRepository.findById(saleId).orElse(null);

        model.addAttribute("sales", sales);

        return "S0022";
    }

    // 編集画面
    @GetMapping("/sales/edit")
    public String edit(Integer saleId, Model model) {

        Sale sales = salesRepository.findById(saleId).orElse(null);

        SalesForm form = new SalesForm();

        form.setSaleId(sales.getSaleId());
        form.setSaleDate(sales.getSaleDate().toString());
        form.setAccountId(sales.getAccountId());
        form.setCategoryId(sales.getCategoryId());
        form.setTradeName(sales.getTradeName());
        form.setUnitPrice(sales.getUnitPrice());
        form.setSaleNumber(sales.getSaleNumber());
        form.setNote(sales.getNote());

        // 更新権限
        form.setAuthority("更新");

        model.addAttribute("salesForm", form);

        return "S0023";
    }

    // 更新処理
    @PostMapping("/sales/update")
    public String update(
    @Valid
    @ModelAttribute
    SalesForm salesForm, BindingResult result, Model model) {

        // 入力エラー
        if (result.hasErrors()) {
            return "S0023";
        }

        // 権限チェック
        if (!"更新".equals(salesForm.getAuthority())) {
            model.addAttribute("errorMessage", "更新権限がありません。");
            return "S0023";
        }

        Sale sales = salesRepository.findById(salesForm.getSaleId()).orElse(null);

        // 更新
        sales.setTradeName(salesForm.getTradeName());
        sales.setUnitPrice(salesForm.getUnitPrice());
        sales.setSaleNumber(salesForm.getSaleNumber());
        sales.setNote(salesForm.getNote());
        salesRepository.save(sales);

        return "redirect:/S0020";
    }
}