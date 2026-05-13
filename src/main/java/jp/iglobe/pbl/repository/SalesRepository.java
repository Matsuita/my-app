package jp.iglobe.pbl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.iglobe.pbl.model.Sales;

public interface SalesRepository
        extends JpaRepository<Sales, Integer> {

    // 商品名部分一致
    List<Sales>
    findByTradeNameContaining(String tradeName);

}