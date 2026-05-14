package jp.iglobe.pbl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.iglobe.pbl.model.SearchAccount;

public interface SearchRepository extends JpaRepository<SearchAccount, Integer> {

	//
	@Query("""
			    SELECT a FROM SearchAccount a
			    WHERE
			    (:name IS NULL OR a.name LIKE %:name%)
			    AND
			    (:mail IS NULL OR a.mail = :mail)
			    AND
			    (:authority IS NULL OR a.authority = :authority)
			""")
	//accountsテーブル）からデータ取得
	//名前：未入力なら条件無視、入力あれば部分一致検索
	//メール：未入力なら条件無視、入力あれば完全一致
	//権限：未選択なら条件無視、選択されていれば一致検索

	List<SearchAccount> search(String name, String mail, Integer authority);
}
