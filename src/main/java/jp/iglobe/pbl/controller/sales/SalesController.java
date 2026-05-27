package jp.iglobe.pbl.controller.sales;

import java.util.List;

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
import jp.iglobe.pbl.model.category.Category;
import jp.iglobe.pbl.model.sales.Sale;
import jp.iglobe.pbl.model.sales.SalesCreateForm;
import jp.iglobe.pbl.model.sales.SalesForm;
import jp.iglobe.pbl.model.sales.SalesSearchForm;
import jp.iglobe.pbl.repository.AccountRepository;
import jp.iglobe.pbl.repository.SalesRepository;
import jp.iglobe.pbl.service.SalesCreateService;
import jp.iglobe.pbl.service.SalesSearchService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SalesController {

	private final AccountRepository accountRepository;
	private final SalesRepository salesRepository;
	
	private final SalesCreateService salesCreateService;
	private final SalesSearchService salesSearchService;

	// 売上登録画面
	@GetMapping("/sales")
	public String newSale(HttpSession session, Model model) {

		Account loginUser = (Account) session.getAttribute("loginUser");
		// 権限チェック
		if (loginUser.getSalesAuthority() != 2) {

			return "redirect:/dashboard";
		}

		// 画面へ渡す
		SalesCreateForm salesCreateForm = new SalesCreateForm();

		model.addAttribute("salesCreateForm", salesCreateForm);
		model.addAttribute("accountList", salesCreateService.getSalesAccounts());
		model.addAttribute("categoryList", salesCreateService.getCategories());

		return "S0010";
	}

	
	// 売上登録確認画面
	@PostMapping("/sales/confirm")
	public String salesConfirm(@Valid SalesCreateForm salesCreateForm, BindingResult result,
								Model model) {
		
		// アカウント存在チェック
		Account account = salesCreateService.validateAccount(
			        salesCreateForm.getAccountId(),result);

		// カテゴリー存在チェック
		Category category = salesCreateService.validateCategory(
		        salesCreateForm.getCategoryId(),result);

		//    	入力エラー
		if (result.hasErrors()) {

			model.addAttribute("salesCreateForm", salesCreateForm);
			model.addAttribute("accountList", salesCreateService.getSalesAccounts());
			model.addAttribute("categoryList", salesCreateService.getCategories());

			return "S0010";
		}

		//		小計
		long total = salesCreateService.calculateTotal(
		        salesCreateForm.getUnitPrice(),salesCreateForm.getSaleNumber());

		model.addAttribute("total", total);
		model.addAttribute("salesCreateForm", salesCreateForm);
		model.addAttribute("accountList", salesCreateService.getSalesAccounts());
		model.addAttribute("categoryList", salesCreateService.getCategories());
		model.addAttribute("account", account);
		model.addAttribute("category", category);
		
		return "S0011";
	}


	//    売上確認→キャンセル(値保持）
	@PostMapping("/sales/back")
	public String back(@ModelAttribute SalesCreateForm salesCreateForm,
						Model model) {
		
		model.addAttribute("salesCreateForm", salesCreateForm);
		model.addAttribute("accountList", salesCreateService.getSalesAccounts());
		model.addAttribute("categoryList", salesCreateService.getCategories());

		return "S0010";
	}


	// 売上登録実行
	@PostMapping("/sales/create")
	public String salesCreate(@Valid SalesCreateForm salesCreateForm,
	        BindingResult result, Model model) {
		
//    	入力エラー
		if (result.hasErrors()) {

			model.addAttribute("salesCreateForm", salesCreateForm);
			model.addAttribute("accountList", salesCreateService.getSalesAccounts());
			model.addAttribute("categoryList", salesCreateService.getCategories());

			return "S0010";
		}

		salesCreateService.createSale(salesCreateForm);

		return "redirect:/sales";
	}

	
	//  直接URL入力の場合、売上登録へ
	@GetMapping({"/sales/confirm","/sales/back","/sales/create"})
	public String getCreate(Model model) {

		model.addAttribute("salesCreateForm", new SalesCreateForm());
		model.addAttribute("accountList", salesCreateService.getSalesAccounts());
		model.addAttribute("categoryList", salesCreateService.getCategories());

		return "redirect:/sales";
	}

	
	// 売上詳細編集確認画面
	@PostMapping("/sales/edit/confirm/{saleId}")
	public String editConfirm(
			@Valid @ModelAttribute SalesForm salesForm, BindingResult result, Model model,
			@PathVariable Integer saleId) {

		// アカウント存在チェック
		Account account = salesCreateService.validateAccount(
					        salesForm.getAccountId(),result);
		// カテゴリー存在チェック
		Category category = salesCreateService.validateCategory(
				        salesForm.getCategoryId(),result);
		
		// 単価形式チェック
		salesCreateService.validatePriceAndNumber(salesForm,result);

		// 入力エラー
		if (result.hasErrors()) {
			
			String accountName = salesCreateService.getAccountBySaleId(saleId);
			
		    model.addAttribute("accountName", accountName);
			model.addAttribute("accountList", salesSearchService.getUpdateAccounts());
			model.addAttribute("categoryList", salesSearchService.getAllCategories());
			model.addAttribute("account", account);
			model.addAttribute("category", category);

			return "S0023";
		}

		account = accountRepository.findById(salesForm.getAccountId()).orElse(null);
		String accountName = salesCreateService.getAccountName(account);
		String categoryName = category.getCategoryName();

		model.addAttribute("salesForm", salesForm);
		model.addAttribute("saleName", accountName);
		model.addAttribute("accountList", salesSearchService.getUpdateAccounts());
		model.addAttribute("categoryName", categoryName);

		return "S0024";
	}
	
	
//  編集確認→キャンセル(値保持）
	@PostMapping("/sales/edit/back/{saleId}")
	public String editBack(@ModelAttribute SalesForm salesForm,
			@PathVariable Integer saleId, Model model) {
		
		String accountName = salesCreateService.getAccountBySaleId(saleId);

	    model.addAttribute("accountName", accountName);
		model.addAttribute("salesForm", salesForm);
		model.addAttribute("accountList", salesSearchService.getUpdateAccounts());
	    model.addAttribute("categoryList", salesSearchService.getAllCategories());

		return "S0023";
	}

	
	// 売上詳細削除確認画面
	@PostMapping("/sales/delete/{saleId}")
	public String deleteConfirm(@PathVariable Integer saleId, Model model) {

		Sale sales = salesRepository.findById(saleId).orElseThrow();
		Account account = accountRepository.findById(sales.getAccountId()).orElse(null);
		String accountName = salesCreateService.getAccountName(account);
		
		long total = salesCreateService.calculateTotal(
			        sales.getUnitPrice(), sales.getSaleNumber());

		model.addAttribute("total", total);
		model.addAttribute("sales", sales);
		model.addAttribute("saleName", accountName);
		model.addAttribute("accountList", accountRepository.findAll());
		model.addAttribute("categoryList", salesSearchService.getAllCategories());

		return "S0025";
	}

	
	//    削除実行
	@PostMapping("/sales/delete/execute")
	public String salesDelete(Integer saleId, HttpSession session) {
		// 削除
		salesRepository.deleteById(saleId);
		// 一覧再取得
		List<Sale> salesList = salesRepository.findAll();
		// session更新
		session.setAttribute("salesList", salesList);
		
		return "redirect:/sales/result";
	}
	
	
	//    直接URL入力の場合、検索画面へ
	@GetMapping({"/sales/edit/confirm/{saleId}","/sales/edit/back/{saleId}",
					"/sales/delete/{saleId}","/sales/delete/execute"})
	public String getEditConfirm(Model model, @PathVariable Integer saleId) {
		
		model.addAttribute("salesSearchForm", new SalesSearchForm());
		//担当一覧
		model.addAttribute("accountList", salesSearchService.getUsedAccounts());
		// カテゴリー一覧
		model.addAttribute("categoryList", salesSearchService.getAllCategories());

		return "redirect:/sales/search";
	}

}