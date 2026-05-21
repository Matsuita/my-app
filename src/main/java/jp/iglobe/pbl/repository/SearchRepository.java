package jp.iglobe.pbl.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.iglobe.pbl.model.account.Account;

public interface SearchRepository extends JpaRepository<Account, Integer> {

	@Query("""
			SELECT a FROM Account a
			WHERE (:name IS NULL OR a.name LIKE %:name%)
			AND (:mail IS NULL OR a.mail LIKE %:mail%)
			AND (:salesAuthority IS NULL OR a.salesAuthority = :salesAuthority)
			AND (:accountsAuthority IS NULL OR a.accountsAuthority = :accountsAuthority)
			AND a.isActive = true
			""")
	List<Account> search(
			@Param("name") String name,
			@Param("mail") String mail,
			@Param("salesAuthority") Integer salesAuthority,
			@Param("accountsAuthority") Integer accountsAuthority);

	@Transactional
	@Modifying
	@Query("""
			UPDATE Account a
			SET a.isActive = false
			WHERE a.accountId = :accountId
			""") // ← 💡ここでis_activeを 0（false）に更新する
	void logicalDeleteById(@Param("accountId") Integer accountId);

}
