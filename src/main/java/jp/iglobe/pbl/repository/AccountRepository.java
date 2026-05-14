package jp.iglobe.pbl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.iglobe.pbl.model.Account;

public interface AccountRepository
        extends JpaRepository<Account, Integer> {

    // ログイン用
    Account findByMail(String mail);

    // 一覧取得
    List<Account> findAll();
}