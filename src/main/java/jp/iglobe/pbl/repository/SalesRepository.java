package jp.iglobe.pbl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.iglobe.pbl.model.sales.Sale;

public interface SalesRepository
        extends JpaRepository<Sale, Integer> {

    // 商品名部分一致
    List<Sale>
    findByTradeNameContaining(String tradeName);

    // 備考部分一致
    List<Sale>
    findByNoteContaining(String note);
    @Query("""

    	    SELECT s
    	    FROM Sale s

    	    WHERE
    	    (:saleDateFrom IS NULL
    	        OR s.saleDate >= :saleDateFrom)

    	    AND
    	    (:saleDateTo IS NULL
    	        OR s.saleDate <= :saleDateTo)

    	    AND
    	    (:accountId IS NULL
    	        OR s.accountId = :accountId)

    	    AND
    	    (:categoryId IS NULL
    	        OR s.categoryId = :categoryId)

    	    AND
    	    (:tradeName IS NULL
    	        OR :tradeName = ''
    	        OR s.tradeName LIKE %:tradeName%)

    	    AND
    	    (:note IS NULL
    	        OR :note = ''
    	        OR s.note LIKE %:note%)

    	""")
    	List<Sale> search(

    	    LocalDate saleDateFrom,

    	    LocalDate saleDateTo,

    	    Integer accountId,

    	    Integer categoryId,

    	    String tradeName,

    	    String note
    	);
    
    //  売上削除
    	List<Sale> deleteBySaleId(Integer saleId);
    	
    	// 売上登録済み担当ID取得
    	@Query("""

    	    SELECT DISTINCT s.accountId
    	    FROM Sale s

    	""")
    	List<Integer> findUsedAccountIds();
    	
    	@Query("""

    			SELECT s
    			FROM Sale s
    			WHERE s.saleDate = :today

    			""")
    			List<Sale> findTodaySales(
    			        LocalDate today);
    	@Query("""

    		    SELECT s.tradeName

    		    FROM Sale s

    		    GROUP BY s.tradeName

    		    ORDER BY SUM(s.saleNumber) DESC

    		""")
    	List<String> findTopSellingProduct();
    	
    	@Query("""

    		    SELECT s.tradeName

    		    FROM Sale s

    		    GROUP BY s.tradeName

    		    ORDER BY SUM(s.saleNumber) ASC


    		""")
    	List<String> findWorstSellingProduct();
    	
    	@Query("""

    		    SELECT s.tradeName

    		    FROM Sale s

    		    GROUP BY s.tradeName

    		    ORDER BY SUM(s.saleNumber) DESC

    		""")
    		List<String> findTopProducts();
    
}
    