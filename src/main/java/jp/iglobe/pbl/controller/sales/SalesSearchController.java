package jp.iglobe.pbl.controller.sales;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	@GetMapping("/sales/search")
	public String search(HttpSession session, Model model) {
		
		

		// 未ログイン
		if (session.getAttribute("loginUser") == null) {
			
			
			
			return "redirect:/";
		}
		
		Account loginUser =
		        (Account) session.getAttribute(
		                "loginUser");

		if(loginUser.getSalesAuthority() < 1){

		    return "redirect:/dashboard";
		}

		model.addAttribute(
				"salesSearchForm",
				new SalesSearchForm());

		// 担当一覧
		List<Integer> accountIds = salesRepository.findUsedAccountIds();

		model.addAttribute(
			    "accountList",
			    accountRepository
			        .findByAccountIdInAndIsActiveTrue(
			            accountIds));

		// カテゴリー一覧
		model.addAttribute(
				"categoryList",
				categoryRepository.findAll());

		return "S0020";
	}

	

	// 検索処理
	@PostMapping("/sales/result")
	public String searchResult(

			HttpSession session,

			@Valid 
			@ModelAttribute 
			SalesSearchForm salesSearchForm,BindingResult result,Model model) {

		// 未ログイン
		if (session.getAttribute("loginUser") == null) {

			return "redirect:/";
		}
		
		Account loginUser =
		        (Account) session.getAttribute(
		                "loginUser");

		if(loginUser.getSalesAuthority() == 0){

		    return "redirect:/dashboard";
		}

		// 開始日チェック

		if (result.hasFieldErrors("saleDateFrom")) {

			model.addAttribute(
					"dateFromError",
					"販売日（検索開始日）を正しく入力して下さい。");

			List<Integer> accountIds =
			        salesRepository.findUsedAccountIds();

			model.addAttribute("accountList", accountRepository .findByAccountIdInAndIsActiveTrue(accountIds));
			model.addAttribute("categoryList", categoryRepository.findAll());

			return "S0020";
		}

		// 終了日チェック

		if (result.hasFieldErrors("saleDateTo")) {

			model.addAttribute(
					"dateToError",
					"販売日（検索終了日）を正しく入力して下さい。");

			List<Integer> accountIds =
			        salesRepository.findUsedAccountIds();

			model.addAttribute("accountList", accountRepository.findByAccountIdInAndIsActiveTrue(accountIds));

			model.addAttribute(
					"categoryList",
					categoryRepository.findAll());

			return "S0020";
		}

		// 開始日 > 終了日チェック

		if (salesSearchForm.getSaleDateFrom() != null
				&& salesSearchForm.getSaleDateTo() != null
				&& salesSearchForm.getSaleDateFrom()
						.isAfter(
								salesSearchForm.getSaleDateTo())) {

			model.addAttribute(
					"dateRangeError",
					"販売日（検索開始日）または販売日（検索終了日）を正しく入力して下さい。");

			List<Integer> accountIds =
			        salesRepository.findUsedAccountIds();

			model.addAttribute(
			        "accountList",
			        accountRepository.findAllById(
			                accountIds));

			model.addAttribute(
					"categoryList",
					categoryRepository.findAll());

			return "S0020";
		}

		// 検索

		List<Sale> salesList = salesRepository.search(

				salesSearchForm.getSaleDateFrom(),

				salesSearchForm.getSaleDateTo(),

				salesSearchForm.getAccountId(),

				salesSearchForm.getCategoryId(),

				salesSearchForm.getTradeName(),

				salesSearchForm.getNote());

		// 件数チェック

		if (salesList.isEmpty()) {

			model.addAttribute(
					"searchError",
					"検索結果はありません。");

			List<Integer> accountIds =
			        salesRepository.findUsedAccountIds();

			model.addAttribute(
			        "accountList",
			        accountRepository.findAllById(
			                accountIds));

			model.addAttribute(
					"categoryList",
					categoryRepository.findAll());

			return "S0020";
		}

		// session保存
		session.setAttribute(
				"salesList",
				salesList);
		session.setAttribute(
		        "searched",
		        true);

		// 結果
		model.addAttribute(
				"salesList",
				salesList);

		model.addAttribute(
		        "accountList",
		        accountRepository.findAll());

		model.addAttribute(
				"categoryList",
				categoryRepository.findAll());

		model.addAttribute(
				"salesSearchForm",
				salesSearchForm);

		return "S0021";
	}

	// 詳細画面
	@GetMapping("/sales/detail/{saleId}")
	public String detail(HttpSession session, @PathVariable
		     Integer saleId, Model model) {

	    if(session.getAttribute("loginUser")
	            == null){

	        return "redirect:/";
	    }
		
		Account loginUser =
		        (Account) session.getAttribute(
		                "loginUser");

		if(loginUser.getSalesAuthority() == 0){

		    return "redirect:/dashboard";
		}

		// 詳細画面へ直接アクセス禁止
		if (session.getAttribute("salesList") == null) {

			return "redirect:/sales/search";
		}

		Sale sales = salesRepository.findById(saleId).orElse(null);
		
		Account account = accountRepository.findById(sales.getAccountId()).orElse(null);

		String accountName;

		if(account == null
		        || !account.isActive()){

		    accountName =
		        "（退職済みユーザー）";

		}else{

		    accountName =
		        account.getName();
		}

		model.addAttribute(
		        "accountName",
		        accountName);

		model.addAttribute("sales", sales);
		
		model.addAttribute(
		        "accountList",
		        accountRepository.findAll());

		model.addAttribute(
				"categoryList",
				categoryRepository.findAll());

		return "S0022";
	}

	// 編集画面
	@GetMapping("/sales/edit/{saleId}")
	public String edit(HttpSession session, @PathVariable Integer saleId,
	        Model model) {
		
		if(session.getAttribute("loginUser")
		        == null){

		    return "redirect:/";
		}

	    Account loginUser =
	            (Account) session.getAttribute(
	                    "loginUser");

	    // 権限チェック
	    if(loginUser.getSalesAuthority() == 0){

	        return "redirect:/dashboard";
	    }else if(loginUser.getSalesAuthority() == 1) {
	    	return "redirect:/sales/search";
	    }
	    
	 // 詳細画面へ直接アクセス禁止
	 		if (session.getAttribute("salesList") == null) {

	 			return "redirect:/sales/search";
	 		}

	    // saleIdなし
	    if(saleId == null) {

	        return "redirect:/sales/result";
	    }

	    // 売上取得
	    Sale sales =
	            salesRepository
	                .findById(saleId)
	                .orElse(null);

	    // 売上なし
	    if(sales == null) {

	        return "redirect:/sales/result";
	    }

	    // アカウント取得
	    Account account = accountRepository
	                .findById(
	                    sales.getAccountId())
	                .orElse(null);

	    // 担当名
	    String accountName;

	    if(account == null
	            || !account.isActive()){

	        accountName =
	                "（退職済みユーザー）";

	    }else{

	        accountName =
	                account.getName();
	    }

	    model.addAttribute(
	            "accountName",
	            accountName);

	    // Formへセット
	    SalesForm form = new SalesForm();

	    form.setSaleId(
	            sales.getSaleId());

	    form.setSaleDate(
	            sales.getSaleDate()
	                .toString());

	    form.setAccountId(
	            sales.getAccountId());

	    form.setCategoryId(
	            sales.getCategoryId());

	    form.setTradeName(
	            sales.getTradeName());

	    form.setUnitPrice(
	            String.valueOf(
	                    sales.getUnitPrice()));

	    form.setSaleNumber(
	            String.valueOf(
	                    sales.getSaleNumber()));

	    form.setNote(
	            sales.getNote());

	    // 更新権限
	    form.setAuthority("更新");

	    model.addAttribute(
	            "salesForm",
	            form);

	    // 担当一覧
	    model.addAttribute(
	            "accountList",
	            accountRepository.findAll());

	    // カテゴリ一覧
	    model.addAttribute(
	            "categoryList",
	            categoryRepository.findAll());

	    return "S0023";
	}

	// 更新処理
	@PostMapping("/sales/update")
	public String update(
			@Valid 
			@ModelAttribute 
			SalesForm salesForm, BindingResult result, Model model, HttpSession session) {
		
		if(session.getAttribute("loginUser")
		        == null){

		    return "redirect:/";
		}
		
		Account loginUser =
		        (Account) session.getAttribute(
		                "loginUser");

		if(loginUser.getSalesAuthority() != 2){

		    return "redirect:/dashboard";
		}

	    
	 // 単価形式チェック
	    if(!salesForm.getUnitPrice().matches("^[0-9]+$")) {

	        model.addAttribute("unitPriceError", "単価を正しく入力して下さい。");
	        
	        model.addAttribute(
	                "accountList",
	                accountRepository.findAll());
	        
	        model.addAttribute("categoryList", categoryRepository.findAll());

	        return "S0023";
	    }
	    
	    // 個数形式チェック
	    if(!salesForm.getSaleNumber().matches("^[0-9]+$")) {
	        model.addAttribute("saleNumberError", "個数を正しく入力して下さい。");
	        
	        model.addAttribute(
	                "accountList",
	                accountRepository.findAll());
	        
	        model.addAttribute("categoryList", categoryRepository.findAll());

	        return "S0023";
	    }
	    

		// 権限チェック
		if (!"更新".equals(salesForm.getAuthority())) {
			model.addAttribute("errorMessage", "更新権限がありません。");
			return "S0023";
		}
		
		
		// 入力エラー
	    if(result.hasErrors()) {
	    	model.addAttribute(
	    	        "accountList",
	    	        accountRepository.findAll());

	        model.addAttribute("categoryList", categoryRepository.findAll());

	        return "S0023";
	    }

		Sale sales = salesRepository.findById(salesForm.getSaleId()).orElse(null);

		// 更新
		sales.setTradeName(salesForm.getTradeName());
		sales.setUnitPrice(Integer.parseInt(salesForm.getUnitPrice()));
		sales.setSaleNumber(Integer.parseInt(salesForm.getSaleNumber()));
		sales.setNote(salesForm.getNote());
		sales.setSaleDate(LocalDate.parse(salesForm.getSaleDate()));
		sales.setAccountId(salesForm.getAccountId());
		sales.setCategoryId(salesForm.getCategoryId());
		salesRepository.save(sales);

		// 一覧再取得
		List<Sale> salesList = salesRepository.findAll();
		session.setAttribute("salesList", salesList);
		return "redirect:/sales/result";
	}

	@GetMapping("/sales/result")
	public String result(HttpSession session, Model model) {
		if(session.getAttribute("loginUser")
		        == null) {

		    return "redirect:/";
		}
		
		model.addAttribute(
		        "accountList",
		        accountRepository.findAll());
		
		if(session.getAttribute("searched")
		        == null){

		    return "redirect:/sales/search";
		}
		
		Account loginUser =
		        (Account) session.getAttribute(
		                "loginUser");

		if(loginUser.getSalesAuthority() < 1){

		    return "redirect:/dashboard";
		}
		
		// 詳細画面へ直接アクセス禁止
 		if (session.getAttribute("salesList") == null) {

 			return "redirect:/sales/search";
 		}

		// 一覧取得
		model.addAttribute(
				"salesList",
				session.getAttribute("salesList"));

		model.addAttribute(
		        "accountList",
		        accountRepository.findAll());
		
		model.addAttribute(
				"categoryList",
				categoryRepository.findAll());

		return "S0021";
	}
}