package jp.iglobe.pbl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.account.AccountForm;
import jp.iglobe.pbl.repository.AccountRepository;

@Service
public class AccountService {
	@Autowired
	AccountRepository accountRepository;

	//	アカウント登録　accountメソッド
	// 新しいフォーム（データ）を準備
	public AccountForm getNewAccountForm() {
		return new AccountForm();
	}

	//	アカウント登録確認　confirmRegister
	public void validateAccountForm(AccountForm accountform, BindingResult result) {

		// パスワード一致チェック（カスタムチェック）
		if (!accountform.getPassword().equals(accountform.getPasswordConfirm())) {
			// passwordConfirmフィールドに対して個別にエラーを紐付ける
			result.rejectValue("passwordConfirm", "error.passwordConfirm", "パスワードとパスワード（確認）の入力値が異なります。");
		}
		// メールアドレス重複チェック（DBを見に行く処理）
		if (accountRepository.existsByMail(accountform.getMail())) {
			result.rejectValue("mail", "error.mail", "このメールアドレスは既に使用されているため、別のメールアドレスで登録してください。");
		}

	}

	//	アカウントの新規登録（DB保存）を行う
	public void registerAccount(AccountForm accountForm) {
		// 1. Repositoryが扱う「Account」クラスに値をセット
		Account account = new Account();
		account.setName(accountForm.getName());
		account.setMail(accountForm.getMail());
		account.setPassword(accountForm.getPassword());
		account.setSalesAuthority(accountForm.getSalesAuthority());
		account.setAccountsAuthority(accountForm.getAccountsAuthority());

		// 2. DB保存を実行
		account.setActive(true);
		accountRepository.save(account); // 👈 エラーなく動きます！
	}
}
