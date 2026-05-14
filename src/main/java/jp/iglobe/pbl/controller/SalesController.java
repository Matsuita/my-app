package jp.iglobe.pbl.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.Account;
import jp.iglobe.pbl.model.Category;
import jp.iglobe.pbl.model.Sale;
import jp.iglobe.pbl.repository.AccountRepository;
import jp.iglobe.pbl.repository.CategoryRepository;
import jp.iglobe.pbl.repository.SalesRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SalesController {

    // 担当
    private final AccountRepository accountRepository;

    // 商品カテゴリ
    private final CategoryRepository categoryRepository;

    // 売上
    private final SalesRepository salesRepository;

    // =========================
    // 売上登録画面
    // =========================

    @GetMapping("/S0010")
    public String newSale(Model model) {

        // 担当一覧
        List<Account> accountList =
                accountRepository.findAll();

        // カテゴリ一覧
        List<Category> categoryList =
                categoryRepository.findAll();

        // 画面へ渡す
        model.addAttribute(
                "sale",
                new Sale());

        model.addAttribute(
                "accountList",
                accountList);

        model.addAttribute(
                "categoryList",
                categoryList);

        return "S0010";
    }

    // =========================
    // 売上登録確認画面
    // =========================

    @PostMapping("/S0011")
    public String salesConfirm(
            Sale sale,
            Model model) {

        model.addAttribute(
                "sale",
                sale);

        return "S0011";
    }

    // =========================
    // 売上登録実行
    // =========================

    @PostMapping("/sales/create")
    public String salesCreate(
            Sale sale) {

        salesRepository.save(sale);

        return "redirect:/S0010";
    }

    // =========================
    // 売上詳細編集確認画面
    // =========================

    @GetMapping("/S0024")
    public String editConfirm(
            Model model) {

        model.addAttribute(
                "sale",
                new Sale());

        return "S0024";
    }

    // =========================
    // 売上詳細削除確認画面
    // =========================

    @GetMapping("/S0025")
    public String salesDelete(
            Model model) {

        model.addAttribute(
                "sale",
                new Sale());

        return "S0025";
    }
}