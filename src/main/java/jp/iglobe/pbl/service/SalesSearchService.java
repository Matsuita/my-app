package jp.iglobe.pbl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.category.Category;
import jp.iglobe.pbl.model.sales.Sale;
import jp.iglobe.pbl.model.sales.SalesForm;
import jp.iglobe.pbl.model.sales.SalesSearchForm;
import jp.iglobe.pbl.repository.AccountRepository;
import jp.iglobe.pbl.repository.CategoryRepository;
import jp.iglobe.pbl.repository.SalesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesSearchService {
    private final SalesRepository salesRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    // 検索

    public List<Sale> search(SalesSearchForm form) {
        return salesRepository.search(
                form.getSaleDateFrom(),
                form.getSaleDateTo(),
                form.getAccountId(),
                form.getCategoryId(),
                form.getTradeName(),
                form.getNote());
    }

    // 詳細

    public Sale findById(Integer saleId) {
        return salesRepository.findById(saleId).orElse(null);
    }

    // 更新

    public void update(SalesForm form) {
        Sale sales = salesRepository.findById(form.getSaleId()).orElse(null);
        if(sales == null) {
            return;
        }

        sales.setTradeName(form.getTradeName());
        sales.setUnitPrice(Integer.parseInt(form.getUnitPrice()));
        sales.setSaleNumber(Integer.parseInt(form.getSaleNumber()));
        sales.setNote(form.getNote());
        sales.setSaleDate(form.getSaleDate());
        sales.setAccountId(form.getAccountId());
        sales.setCategoryId(form.getCategoryId());
        salesRepository.save(sales);
    }

    // 一覧

  public List<Sale> findAll() {
    return salesRepository.findAllByOrderBySaleIdAsc();
}
    
    
    // 全アカウント

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // 使用担当一覧
    
    public List<Account>
        getUsedAccounts() {
        List<Integer> accountIds = salesRepository.findUsedAccountIds();
        return accountRepository.findByAccountIdInAndIsActiveTrue(accountIds);
    }

    
 // 担当一覧
    public List<Account> getUpdateAccounts() {

        return accountRepository.findAll();
    }

    // カテゴリ一覧

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 日付範囲チェック

    public boolean isInvalidDateRange(SalesSearchForm form) {

        return form.getSaleDateFrom() != null
                &&
                form.getSaleDateTo() != null
                &&
                form.getSaleDateFrom().isAfter(form.getSaleDateTo());
    }

    // 担当名取得

    public String getAccountName(Integer accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);

        if(account == null || !account.isActive()) {
            return "（退職済みユーザー）";
        }

        return account.getName();
    }
}
