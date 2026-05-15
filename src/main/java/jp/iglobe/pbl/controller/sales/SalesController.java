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
        model.addAttribute("sale", new Sale());

        model.addAttribute("accountList",accountList);

        model.addAttribute("categoryList",categoryList);

        return "S0010";
    }

    // 売上登録確認画面

    @PostMapping("/S0011")
    public String salesConfirm(
            Sale sale, Model model) {
    	List<Account> accountList =
                accountRepository.findAll();
    	
    	List<Category> categoryList =
                categoryRepository.findAll();
    	
    	if(sale == null) {
    		
    	}
    	
        model.addAttribute("sale", sale);
        
        model.addAttribute("accountList", accountList);
        
        model.addAttribute("categoryList",categoryList);
        
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

        model.addAttribute(
                "salesForm",
                salesForm);

        return "S0024";
    }

    // 売上詳細削除確認画面

    @PostMapping("/sales/delete")
    public String deleteConfirm(Sale sales, Model model) {

        model.addAttribute("sales", sales);

        return "S0025";
    }
    
    @PostMapping("/sales/delete/execute")
    public String salesDelete(Integer saleId) {
    	
    	salesRepository.deleteBySaleId(saleId);
    	
    	return "S0021";
    }
}