package jp.iglobe.pbl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.category.Category;
import jp.iglobe.pbl.model.sales.Sale;
import jp.iglobe.pbl.model.sales.SalesCreateForm;
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
	
	// 担当一覧(売上権限なし・退職を省く）
	public List<Account> getSalesAccounts() {

        return accountRepository
                .findBySalesAuthorityAndIsActive(2, true);
    }
		
	// カテゴリ一覧
	public List<Category> getCategories() {

        return categoryRepository.findAll();
    }


	public Account validateAccount(Integer accountId, BindingResult result){
	// アカウント存在チェック
	
	if (accountId == null) {
        return null;
    }
	Account account = accountRepository.findById(
			accountId).orElse(null);
	
	if (account == null) {

		result.rejectValue("accountId", null,
				"アカウントテーブルに存在しません。");
	}else if(!account.isActive()){
		result.rejectValue("accountId", null,
				"退職済みユーザーです。");
	}

		return account;
}

//小計計算
	public long calculateTotal(String unitPrice, String saleNumber){
	
		Integer price = Integer.parseInt(unitPrice);
			Integer number = Integer.parseInt(saleNumber);
			return (long) price * number;
	}

	public long calculateTotal(Integer unitPrice,Integer saleNumber) {

    return (long) unitPrice * saleNumber;
}

	public Category validateCategory(Integer categoryId, BindingResult result){
	// カテゴリー存在チェック
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

	public void createSale(SalesCreateForm salesCreateForm){
//売上登録実行
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

	public String getAccountName(Account account) {

    if (account != null && account.isActive()) {
        return account.getName();
    }

    return "（退職済みユーザー）";
}

}



