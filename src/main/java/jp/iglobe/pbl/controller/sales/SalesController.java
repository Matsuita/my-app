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
	@GetMapping("/sales")
	public String newSale(HttpSession session, Model model) {

		Account loginUser = (Account) session.getAttribute("loginUser");
		// 権限チェック
		if (loginUser.getSalesAuthority() != 2) {

			return "redirect:/dashboard";
		}

		// 担当一覧(売上権限なし・退職を省く）
		List<Account> accountList = accountRepository.findBySalesAuthorityAndIsActive(2, true);
		// カテゴリ一覧
		List<Category> categoryList = categoryRepository.findAll();

		// 画面へ渡す
		SalesCreateForm salesCreateForm = new SalesCreateForm();

		model.addAttribute("salesCreateForm", salesCreateForm);
		model.addAttribute("accountList", accountList);
		model.addAttribute("categoryList", categoryList);

		return "S0010";
	}

	
	// 売上登録確認画面
	@PostMapping("/sales/confirm")
	public String salesConfirm(HttpSession session, @Valid SalesCreateForm salesCreateForm, BindingResult result,
			Model model) {
		
		List<Account> accountList = accountRepository.findBySalesAuthorityAndIsActive(2, true);
		List<Category> categoryList = categoryRepository.findAll();

		// アカウント存在チェック
		Account account = null;
		if (salesCreateForm.getAccountId() != null) {

			account = accountRepository.findById(
					salesCreateForm.getAccountId()).orElse(null);

			if (account == null) {

				result.rejectValue("accountId", null,
						"アカウントテーブルに存在しません。");
			}else if(!account.isActive()){
				result.rejectValue("accountId", null,
						"退職済みユーザーです。");
			}
		}
		// カテゴリー存在チェック
		Category category = null;
		if (salesCreateForm.getCategoryId() != null) {

			category = categoryRepository.findById(
					salesCreateForm.getCategoryId()).orElse(null);

			if (category == null) {
				result.rejectValue("categoryId", null,
						"商品カテゴリーテーブルに存在しません。");
			}
		}

		//    	入力エラー
		if (result.hasErrors()) {

			model.addAttribute("salesCreateForm", salesCreateForm);
			model.addAttribute("accountList", accountList);
			model.addAttribute("categoryList", categoryList);

			return "S0010";
		}

		Integer unitPrice = Integer.parseInt(salesCreateForm.getUnitPrice());
		Integer saleNumber = Integer.parseInt(salesCreateForm.getSaleNumber());
		long total = (long) unitPrice * saleNumber;

		model.addAttribute("total", total);
		model.addAttribute("salesCreateForm", salesCreateForm);
		model.addAttribute("accountList", accountList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("account", account);
		model.addAttribute("category", category);
		

		return "S0011";
	}


	//    売上確認→キャンセル(値保持）
	@PostMapping("/sales/back")
	public String back(HttpSession session,
			@ModelAttribute SalesCreateForm salesCreateForm, Model model) {
		List<Account> accountList = accountRepository
				.findBySalesAuthorityAndIsActive(2, true);
		List<Category> categoryList = categoryRepository.findAll();

		model.addAttribute("salesCreateForm", salesCreateForm);
		model.addAttribute("accountList", accountList);
		model.addAttribute("categoryList", categoryList);

		return "S0010";
	}


	// 売上登録実行
	@PostMapping("/sales/create")
	public String salesCreate(
			HttpSession session,
			SalesCreateForm salesCreateForm) {

		//    	String→Integer変換
		Sale sale = new Sale();
		sale.setSaleDate(salesCreateForm.getSaleDate());
		sale.setAccountId(salesCreateForm.getAccountId());
		sale.setCategoryId(salesCreateForm.getCategoryId());
		sale.setTradeName(salesCreateForm.getTradeName());
		sale.setUnitPrice(Integer.parseInt(salesCreateForm.getUnitPrice()));
		sale.setSaleNumber(Integer.parseInt(salesCreateForm.getSaleNumber()));
		sale.setNote(salesCreateForm.getNote());

		salesRepository.save(sale);

		session.removeAttribute("salesCreateForm");

		return "redirect:/sales";
	}

	
	//  直接URL入力の場合、売上登録へ
	@GetMapping({"/sales/confirm","/sales/back","/sales/create"})
	public String getCreate(Model model) {
		List<Account> accountList = accountRepository.findBySalesAuthorityAndIsActive(2, true);
		List<Category> categoryList = categoryRepository.findAll();

		model.addAttribute("salesCreateForm", new SalesCreateForm());
		model.addAttribute("accountList", accountList);
		model.addAttribute("categoryList", categoryList);

		return "redirect:/sales";
	}

	
	// 売上詳細編集確認画面
	@PostMapping("/sales/edit/confirm/{saleId}")
	public String editConfirm(
			@Valid @ModelAttribute SalesForm salesForm, BindingResult result, Model model,
			@PathVariable Integer saleId) {

		// アカウント存在チェック
		Account account = null;
		if (salesForm.getAccountId() != null) {

			account = accountRepository.findById(
					salesForm.getAccountId()).orElse(null);

			if (account == null) {
				result.rejectValue("accountId", null,
						"アカウントテーブルに存在しません。");
			}
		}
		// カテゴリ存在チェック
		Category category = null;
		if (salesForm.getCategoryId() != null) {

			category = categoryRepository.findById(
					salesForm.getCategoryId()).orElse(null);

			if (category == null) {
				result.rejectValue("categoryId", null,
						"商品カテゴリーテーブルに存在しません。");
			}
		}
		
		// 単価形式チェック
		if (!salesForm.getUnitPrice()
						.matches("(^$)|^[0-9]+$")) {
					
			result.rejectValue("unitPrice", null,
					"単価を正しく入力してください。");
		}
		// 個数形式チェック
		if (!salesForm.getSaleNumber()
						.matches("(^$)|^([1-9][0-9]*)$")) {
					
			result.rejectValue("saleNumber", null,
					"個数を正しく入力して下さい。");
		}

		// 入力エラー
		if (result.hasErrors()) {
			
			Sale sales = salesRepository.findById(saleId).orElse(null);
			account = accountRepository.findById(sales.getAccountId())
	              .orElse(null);
			
		    String accountName;
		    if(account == null || !account.isActive()){

		        accountName = "（退職済みユーザー）";
		    }else{
		    	accountName = account.getName();
		    }
			
		    model.addAttribute("accountName", accountName);
			model.addAttribute("accountList",
					accountRepository.findBySalesAuthority(2));
			model.addAttribute("categoryList",
					categoryRepository.findAll());

			return "S0023";
		}

		account = accountRepository.findById(salesForm.getAccountId()).orElse(null);
		String accountName;

		if (account != null && account.isActive()) {
			accountName = account.getName();
		} else {
			accountName = "（退職済みユーザー）";
		}

		model.addAttribute("salesForm", salesForm);
		model.addAttribute("saleName", accountName);
		model.addAttribute("accountList",
				accountRepository.findBySalesAuthority(2));
		model.addAttribute("categoryList",
				categoryRepository.findAll());

		return "S0024";
	}
	
	
//  編集確認→キャンセル(値保持）
	@PostMapping("/sales/edit/back/{saleId}")
	public String editBack(HttpSession session,
			@ModelAttribute SalesForm salesForm,@PathVariable Integer saleId, Model model) {
		
		Sale sales = salesRepository.findById(saleId).orElse(null);
		Account account = accountRepository.findById(sales.getAccountId())
              .orElse(null);
		
	    String accountName;
	    if(account == null || !account.isActive()){

	        accountName = "（退職済みユーザー）";
	    }else{
	    	accountName = account.getName();
	    }

	    model.addAttribute("accountName", accountName);
		model.addAttribute("salesForm", salesForm);
		model.addAttribute("accountList", accountRepository.findBySalesAuthority(2));
	    model.addAttribute("categoryList", categoryRepository.findAll());

		return "S0023";
	}

	
	// 売上詳細削除確認画面
	@PostMapping("/sales/delete/{saleId}")
	public String deleteConfirm(@PathVariable Integer saleId, Model model) {

		Sale sales = salesRepository.findById(saleId).orElseThrow();
		Account account = accountRepository.findById(sales.getAccountId()).orElse(null);
		String accountName;

		if (account != null && account.isActive()) {
			accountName = account.getName();
		} else {
			accountName = "（退職済みユーザー）";
		}
		
		long unitPrice = sales.getUnitPrice().longValue();
	    long saleNumber = sales.getSaleNumber().longValue();
		long total = unitPrice * saleNumber;

		model.addAttribute("total", total);
		model.addAttribute("sales", sales);
		model.addAttribute("saleName", accountName);
		model.addAttribute("accountList",
				accountRepository.findAll());
		model.addAttribute("categoryList",
				categoryRepository.findAll());

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
		List<Integer> accountIds = salesRepository.findUsedAccountIds();
		model.addAttribute("accountList", accountRepository
				.findByAccountIdInAndIsActiveTrue(accountIds));
		// カテゴリー一覧
		model.addAttribute("categoryList", categoryRepository.findAll());

		return "redirect:/sales/search";
	}


}