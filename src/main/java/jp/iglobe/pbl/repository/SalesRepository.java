package jp.iglobe.pbl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.iglobe.pbl.model.Sales;

public interface SalesRepository extends JpaRepository<Sales, Integer>{

	List<Sales> findByTradeNameContaining(String tradeName);

}
