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
    	        OR s.tradeName LIKE %:tradeName%)

    	    AND
    	    (:note IS NULL
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
    
}
    