package jp.iglobe.pbl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.iglobe.pbl.model.Sale;

public interface SalesRepository extends JpaRepository<Sale, Integer>{

	List<Sales> findByTradeNameContaining(String tradeName);

}
