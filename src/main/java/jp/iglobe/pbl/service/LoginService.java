package jp.iglobe.pbl.service;

import org.springframework.stereotype.Service;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final AccountRepository accountRepository;

	// ログイン認証
	public Account login(String mail, String password) {

		// メール検索
		Account account = accountRepository.findByMail(mail);

		// 存在しない
		if (account == null) {
			return null;
		}

		// 論理削除
		if (!account.isActive()) {
			return null;
		}

		// パスワード不一致
		if (!account.getPassword().equals(password)) {
			return null;
		}

		return account;
	}
}
