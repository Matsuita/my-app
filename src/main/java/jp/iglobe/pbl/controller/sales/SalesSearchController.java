package jp.iglobe.pbl.controller.sales;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.sales.Sale;
import jp.iglobe.pbl.model.sales.SalesForm;
import jp.iglobe.pbl.model.sales.SalesSearchForm;
import jp.iglobe.pbl.service.SalesSearchService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SalesSearchController {

    private final SalesSearchService salesSearchService;

    // =========================
    // 検索画面
    // =========================

    @GetMapping("/sales/search")
    public String search(HttpSession session, Model model) {

        // 未ログイン
        if(session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }

        Account loginUser = (Account) session.getAttribute("loginUser");

        // 権限
        if(loginUser.getSalesAuthority() < 1) {
            return "redirect:/dashboard";
        }

        // session削除
        session.removeAttribute("searched");
        session.removeAttribute("salesList");
        model.addAttribute("salesSearchForm", new SalesSearchForm());

        // 担当一覧
        model.addAttribute("accountList", salesSearchService.getUsedAccounts());

        // カテゴリ一覧
        model.addAttribute("categoryList", salesSearchService.getAllCategories());
        return "S0020";
    }

    // =========================
    // 検索処理
    // =========================

    @PostMapping("/sales/result")
    public String searchResult(HttpSession session,
            @Valid
            @ModelAttribute
            SalesSearchForm salesSearchForm,
            BindingResult result,Model model) {

        // 未ログイン
        if(session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }

        Account loginUser = (Account) session.getAttribute("loginUser");

        // 権限
        if(loginUser.getSalesAuthority() == 0) {
            return "redirect:/dashboard";
        }

        // 日付範囲
        if(salesSearchService.isInvalidDateRange(salesSearchForm)) {
            model.addAttribute("dateRangeError", "販売日（検索開始日）または販売日（検索終了日）を正しく入力して下さい。");
        }

        // エラー
        if(result.hasErrors() || model.containsAttribute("dateRangeError")) {
            model.addAttribute("accountList", salesSearchService.getUsedAccounts());
            model.addAttribute("categoryList", salesSearchService.getAllCategories());
            return "S0020";
        }

        // 検索
        List<Sale> salesList = salesSearchService.search(salesSearchForm);

        // 0件
        if(salesList.isEmpty()) {
            model.addAttribute("searchError","検索結果はありません。");
            model.addAttribute("accountList",salesSearchService.getUsedAccounts());
            model.addAttribute("categoryList",salesSearchService.getAllCategories());
            return "S0020";
        }

        // session保存
        session.setAttribute("salesList",salesList);
        session.setAttribute("searched",true);

        // 結果
        model.addAttribute("salesList",salesList);
        model.addAttribute("accountList",salesSearchService.getAllAccounts());
        model.addAttribute("categoryList",salesSearchService.getAllCategories());

        return "S0021";
    }

    // =========================
    // 結果画面
    // =========================

    @GetMapping("/sales/result")
    public String result(HttpSession session,HttpServletRequest request,
            Model model) {

        // 未ログイン
        if(session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }

        Account loginUser = (Account) session.getAttribute("loginUser");

        // 権限
        if(loginUser.getSalesAuthority() == 0) {
            return "redirect:/dashboard";
        }
        
        if(loginUser.getSalesAuthority() == 1) {
            return "redirect:/sales/search";
        }

        // URL直打ち
        String referer = request.getHeader("Referer");
        if(referer == null) {
            return "redirect:/sales/search";
        }

        // sessionなし
        if(session.getAttribute("searched") == null
                ||
           session.getAttribute("salesList") == null) {
            return "redirect:/sales/search";
        }

        model.addAttribute("salesList",session.getAttribute("salesList"));
        model.addAttribute("accountList",salesSearchService.getAllAccounts());
        model.addAttribute("categoryList",salesSearchService.getAllCategories());

        return "S0021";
    }

    // =========================
    // 詳細画面
    // =========================

    @GetMapping("/sales/detail/{saleId}")
    public String detail(HttpSession session,HttpServletRequest request,
            @PathVariable
            Integer saleId,
            Model model) {

        // 未ログイン
        if(session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }

        Account loginUser = (Account) session.getAttribute("loginUser");

        // 権限
        if(loginUser.getSalesAuthority() == 0) {
            return "redirect:/dashboard";
        }

        if(loginUser.getSalesAuthority() == 1) {
            return "redirect:/sales/search";
        }

        // URL直打ち
        String referer = request.getHeader("Referer");

        if(referer == null) {
            return "redirect:/sales/search";
        }

        // sessionなし
        if(session.getAttribute("searched") == null
                ||
           session.getAttribute("salesList") == null) {

            return "redirect:/sales/search";
        }

        // 売上取得
        Sale sales = salesSearchService.findById(saleId);

        // 存在しない
        if(sales == null) {
            return "redirect:/sales/result";
        }

        model.addAttribute("accountName", salesSearchService.getAccountName(sales.getAccountId()));
        model.addAttribute("sales", sales);
        model.addAttribute("accountList",salesSearchService.getAllAccounts());
        model.addAttribute("categoryList",salesSearchService.getAllCategories());

        return "S0022";
    }

    // =========================
    // 編集画面
    // =========================

    @GetMapping("/sales/edit/{saleId}")
    public String edit(HttpSession session, HttpServletRequest request,
            @PathVariable
            Integer saleId, Model model) {

        // 未ログイン
        if(session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }

        Account loginUser = (Account) session.getAttribute("loginUser");

        // 権限
        if(loginUser.getSalesAuthority() == 0) {

            return "redirect:/dashboard";
        }
        if(loginUser.getSalesAuthority() == 1) {

            return "redirect:/sales/search";
        }

        // URL直打ち
        String referer = request.getHeader("Referer");

        if(referer == null) {
            return "redirect:/sales/search";
        }

        // sessionなし
        if(session.getAttribute("searched") == null
                ||
           session.getAttribute("salesList") == null) {

            return "redirect:/sales/search";
        }

        // 売上取得
        Sale sales = salesSearchService.findById(saleId);

        // 存在しない
        if(sales == null) {
            return "redirect:/sales/result";
        }

        // 担当名
        model.addAttribute("accountName", salesSearchService.getAccountName(
                        sales.getAccountId()));
        // Form
        SalesForm form = new SalesForm();
        form.setSaleId(sales.getSaleId());
        form.setSaleDate(sales.getSaleDate());
        form.setAccountId(sales.getAccountId());
        form.setCategoryId(sales.getCategoryId());
        form.setTradeName(sales.getTradeName());
        form.setUnitPrice(String.valueOf(sales.getUnitPrice()));
        form.setSaleNumber(String.valueOf(sales.getSaleNumber()));
        form.setNote(sales.getNote());
        model.addAttribute("salesForm",form);

        // 担当一覧
        model.addAttribute("accountList", salesSearchService.getUpdateAccounts());

        // カテゴリ一覧
        model.addAttribute("categoryList", salesSearchService.getAllCategories());

        return "S0023";
    }
    
 // =========================
 // 更新処理
 // =========================

 @PostMapping("/sales/update")
 public String update(HttpSession session,
         @ModelAttribute
         SalesForm salesForm, Model model) {

     // 未ログイン
     if(session.getAttribute("loginUser") == null) {

         return "redirect:/";
     }

     Account loginUser = (Account) session.getAttribute("loginUser");

     // 権限
     if(loginUser.getSalesAuthority() != 2) {
         return "redirect:/dashboard";
     }

     // 更新
     salesSearchService.update(salesForm);

     // 最新一覧取得
     List<Sale> salesList = salesSearchService.findAll();

     // session更新
     session.setAttribute("salesList", salesList);
     session.setAttribute("searched", true);
     return "redirect:/sales/result";
 }
}