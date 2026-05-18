package jp.iglobe.pbl.controller.sales;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.category.Category;
import jp.iglobe.pbl.model.sales.Sale;
import jp.iglobe.pbl.model.sales.SalesCreateForm;
import jp.iglobe.pbl.model.sales.SalesForm;
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

    // 売上登録画面

    @GetMapping("/S0010")
    public String newSale(Model model) {

        // 担当一覧
        List<Account> accountList =
                accountRepository.findAll();

        // カテゴリ一覧
        List<Category> categoryList =
                categoryRepository.findAll();

        // 画面へ渡す
        model.addAttribute("salesCreateForm", new Sale());
        model.addAttribute("accountList",accountList);
        model.addAttribute("categoryList",categoryList);

        return "S0010";
    }

    // 売上登録確認画面

    @PostMapping("/S0011")
    public String salesConfirm(@Valid
            SalesCreateForm salesCreateForm, BindingResult result, Model model) {
    	List<Account> accountList =
                accountRepository.findAll();
    	
    	List<Category> categoryList =
                categoryRepository.findAll();
    	
    	// アカウント存在チェック
    	Account account = null;

    	if(salesCreateForm.getAccountId() != null) {

    	    account =
    	            accountRepository.findById(
    	                    salesCreateForm.getAccountId())
    	                    .orElse(null);
    	}

    	// カテゴリ存在チェック
    	Category category = null;

    	if(salesCreateForm.getCategoryId() != null) {

    	    category =
    	            categoryRepository.findById(
    	                    salesCreateForm.getCategoryId())
    	                    .orElse(null);
    	}

    	// アカウント存在しない
    	if(account == null) {

    	    result.rejectValue(
    	            "accountId",
    	            null,
    	            "アカウントテーブルに存在しません。");
    	}

    	// 商品カテゴリ存在しない
    	if(category == null) {

    	    result.rejectValue(
    	            "categoryId",
    	            null,
    	            "商品カテゴリーテーブルに存在しません。");
    	}

    	
    	if(result.hasErrors()) {
    		
    		model.addAttribute("salesCreateForm", salesCreateForm);
    		
    		model.addAttribute("accountList", accountList);
            
            model.addAttribute("categoryList",categoryList);

    		return "S0010";
    	}
    	
//    	String→Integer変換
    	Sale sale = new Sale();
    	sale.setUnitPrice(Integer.parseInt(salesCreateForm.getUnitPrice()));
        sale.setSaleNumber(Integer.parseInt(salesCreateForm.getSaleNumber()));
    	
        model.addAttribute("sale", sale);
        
        model.addAttribute("accountList", accountList);
        
        model.addAttribute("categoryList",categoryList);
        
        model.addAttribute("account", account);
        
        model.addAttribute("category", category);
        
        return "S0011";
    }

    // 売上登録実行

    @PostMapping("/salesCreate")
    public String salesCreate(Sale sale) {

        salesRepository.save(sale);

        return "redirect:/S0010";
    }

    // 売上詳細編集確認画面

 // 売上詳細編集確認画面
    @PostMapping("/S0024")
    public String editConfirm(
    		@Valid
            @ModelAttribute
            SalesForm salesForm, BindingResult result, Model model) {
    	
    	// アカウント存在チェック
    	Account account = null;

    	if(salesForm.getAccountId() != null) {

    	    account = accountRepository.findById(
    	                    salesForm.getAccountId())
    	                    .orElse(null);
    	}

    	// カテゴリ存在チェック
    	Category category = null;

    	if(salesForm.getCategoryId() != null) {

    	    category =categoryRepository.findById(
    	                    salesForm.getCategoryId())
    	                    .orElse(null);
    	 // アカウント存在しない
    	    if(account == null) {

    	        result.rejectValue(
    	                "accountId",
    	                null,
    	                "アカウントテーブルに存在しません。");
    	    }

    	    // 商品カテゴリ存在しない
    	    if(category == null) {

    	        result.rejectValue(
    	                "categoryId",
    	                null,
    	                "商品カテゴリーテーブルに存在しません。");
    	    }
    	}

        // 入力エラー
        if(result.hasErrors()){

            model.addAttribute(
                    "accountList",
                    accountRepository.findAll());

            model.addAttribute(
                    "categoryList",
                    categoryRepository.findAll());

            return "S0023";
        }
        
     // 単価形式チェック
        if(!salesForm.getUnitPrice()
                .matches("^[0-9]+$")) {

            model.addAttribute(
                    "unitPriceError",
                    "単価を正しく入力して下さい。");

            model.addAttribute(
                    "accountList",
                    accountRepository.findAll());

            model.addAttribute(
                    "categoryList",
                    categoryRepository.findAll());

            return "S0023";
        }
        
     // 個数形式チェック
        if(!salesForm.getSaleNumber()
                .matches("^[0-9]+$")) {

            model.addAttribute(
                    "saleNumberError",
                    "個数を正しく入力して下さい。");

            model.addAttribute(
                    "accountList",
                    accountRepository.findAll());

            model.addAttribute(
                    "categoryList",
                    categoryRepository.findAll());

            return "S0023";
        }

        model.addAttribute(
                "salesForm",
                salesForm);
     model.addAttribute("accountList", accountRepository.findAll());
     model.addAttribute("categoryList", categoryRepository.findAll());
     
        return "S0024";
    }

    // 売上詳細削除確認画面

    @PostMapping("/sales/delete")
    public String deleteConfirm(Integer saleId, Model model) {

    	Sale sales = salesRepository.findById(saleId).orElse(null);
    	
        model.addAttribute("sales", sales);
        
        model.addAttribute("accountList", accountRepository.findAll());

        model.addAttribute("categoryList", categoryRepository.findAll());

        return "S0025";
    }
    
    @PostMapping("/sales/delete/execute")
    public String salesDelete(Integer saleId) {
    	
    	salesRepository.deleteById(saleId);
    	
    	return "redirect:/sales/search";
    }
}