package jp.iglobe.pbl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.category.Category;
import jp.iglobe.pbl.model.sales.Sale;
import jp.iglobe.pbl.model.sales.SalesCreateForm;
import jp.iglobe.pbl.model.sales.SalesForm;
import jp.iglobe.pbl.repository.AccountRepository;
import jp.iglobe.pbl.repository.CategoryRepository;
import jp.iglobe.pbl.repository.SalesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesCreateService {
	private final AccountRepository accountRepository;
	private final CategoryRepository categoryRepository;
	private final SalesRepository salesRepository;

	// 担当一覧(）
	public List<Account> getSalesAccounts() {

		List<Account> accountList = accountRepository.findAll();

	    for (Account account : accountList) {
	    		account.setName(getDisplayName(account));
	    }

	    return accountList;
	}
	
//	プルダウン表示
	public String getDisplayName(Account account) {

	    if (account != null && account.isActive()) {
	        return account.getName();
	    }

	    return account.getName() + "（退職済み）";
	}
	
	public String getAccountBySaleId(Integer saleId) {
		
	Sale sales = salesRepository.findById(saleId).orElse(null);
	Account account = accountRepository.findById(sales.getAccountId())
          .orElse(null);
	 return getAccountName(account);
	 
	}

	// カテゴリ一覧
	public List<Category> getCategories() {

		return categoryRepository.findAll();
	}

	// アカウント存在チェック(論理削除なし）
	public Account validateAccount(Integer accountId, BindingResult result) {
		
		if (accountId == null) {
			return null;
		}
		Account account = accountRepository.findById(
				accountId).orElse(null);

		if (account == null) {

			result.rejectValue("accountId", null,
					"アカウントテーブルに存在しません。");
		}

		return account;
	}
	

	//小計計算
	public long calculateTotal(String unitPrice, String saleNumber) {

		Integer price = Integer.parseInt(unitPrice);
		Integer number = Integer.parseInt(saleNumber);
		return (long) price * number;
	}

	public long calculateTotal(Integer unitPrice, Integer saleNumber) {

		return (long) unitPrice * saleNumber;
	}

	// カテゴリー存在チェック
	public Category validateCategory(Integer categoryId, BindingResult result) {
		
		if (categoryId == null) {
			return null;
		}

		Category category = categoryRepository.findById(
				categoryId).orElse(null);

		if (category == null) {
			result.rejectValue("categoryId", null,
					"商品カテゴリーテーブルに存在しません。");
		}

		return category;
	}
	
	// 単価形式チェック
	public void validatePriceAndNumber(SalesForm salesForm,
	        BindingResult result) {
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
	}

	//売上登録実行
	public void createSale(SalesCreateForm salesCreateForm) {
		
		Sale sale = new Sale();
		sale.setSaleDate(salesCreateForm.getSaleDate());
		sale.setAccountId(salesCreateForm.getAccountId());
		sale.setCategoryId(salesCreateForm.getCategoryId());
		sale.setTradeName(salesCreateForm.getTradeName());
		sale.setUnitPrice(Integer.parseInt(salesCreateForm.getUnitPrice()));
		sale.setSaleNumber(Integer.parseInt(salesCreateForm.getSaleNumber()));
		sale.setNote(salesCreateForm.getNote());

		salesRepository.save(sale);
	}

//	退職ユーザー表示
	public String getAccountName(Account account) {

		if (account != null && account.isActive()) {
			return account.getName();
		}

		return "（退職済みユーザー）";
	}
	


}
