package jp.iglobe.pbl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.iglobe.pbl.model.account.Account;

@Repository
public interface AccountRepository
		extends JpaRepository<Account, Integer> {

	// ログイン用
	Account findByMail(String mail);

	// 一覧取得
	List<Account> findAll();
	
//	テーブル検索
	Optional<Account> findById(Integer accountId);
}