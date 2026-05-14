package jp.iglobe.pbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.iglobe.pbl.model.Sale;

public interface SalesRepository extends JpaRepository<Sale, Integer>{

}
