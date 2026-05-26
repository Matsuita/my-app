package jp.iglobe.pbl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.account.AccountSearchForm;
import jp.iglobe.pbl.model.account.AccountUpdateForm;
import jp.iglobe.pbl.repository.SearchRepository;

@Service
public class SearchAccountService {

    @Autowired
    private SearchRepository searchRepository;

    public List<Account> search(AccountSearchForm form) {

        if (form.getSalesAuthority() != null && form.getSalesAuthority().isEmpty()) {
            form.setSalesAuthority(null);
        }
        if (form.getAccountsAuthority() != null && form.getAccountsAuthority().isEmpty()) {
            form.setAccountsAuthority(null);
        }

        return searchRepository.search(
                form.getName(),
                form.getMail(),
                form.getSalesAuthority(),
                form.getAccountsAuthority());
    }

    public Account getAccountById(Integer id) {
        return searchRepository.findById(id).orElseThrow();
    }

    public boolean existsMail(Integer accountId, String mail) {
        return searchRepository.existsByMailAndAccountIdNot(mail, accountId);
    }

    public void update(AccountUpdateForm form) {
        Account existing = searchRepository
                .findById(form.getAccountId())
                .orElseThrow();

        existing.setName(form.getName());
        existing.setMail(form.getMail());
        existing.setSalesAuthority(form.getSalesAuthority());
        existing.setAccountsAuthority(form.getAccountsAuthority());
        existing.setPassword(form.getPassword());

        searchRepository.save(existing);
    }

    public void logicalDelete(Integer accountId) {
        searchRepository.logicalDeleteById(accountId);
    }
    
    public AccountUpdateForm getAccountUpdateFormById(Integer id) {
        Account account = getAccountById(id);

        AccountUpdateForm form = new AccountUpdateForm();
        form.setAccountId(account.getAccountId());
        form.setName(account.getName());
        form.setMail(account.getMail());
        form.setSalesAuthority(account.getSalesAuthority());
        form.setAccountsAuthority(account.getAccountsAuthority());

        return form;
    }
    
    public void validateForConfirm(AccountUpdateForm form, BindingResult result) {

        // パスワード一致
        if (!result.hasFieldErrors("password")
                && !result.hasFieldErrors("passwordConfirm")) {

            if (!form.getPassword().equals(form.getPasswordConfirm())) {
                result.rejectValue("passwordConfirm", null, "パスワードが一致していません。");
            }
        }

        // メール重複
        if (!result.hasErrors()) {
            if (existsMail(form.getAccountId(), form.getMail())) {
                result.rejectValue("mail", "error.mail",
                        "このメールアドレスは既に使用されています");
            }
        }
    }
}