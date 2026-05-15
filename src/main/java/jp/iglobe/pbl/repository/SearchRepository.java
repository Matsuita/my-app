package jp.iglobe.pbl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.iglobe.pbl.model.account.Account;

public interface SearchRepository extends JpaRepository<Account, Integer> {

	
	@Query("""
			SELECT a FROM Account a
			WHERE (:name IS NULL OR a.name LIKE %:name%)
			AND (:mail IS NULL OR a.mail LIKE %:mail%)
			AND (:authority IS NULL OR a.authority = :authority)
			""")
			List<Account> search(
			    @Param("name") String name,
			    @Param("mail") String mail,
			    @Param("authority") Integer authority);

	
}
