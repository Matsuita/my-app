package jp.iglobe.pbl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.iglobe.pbl.model.category.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Integer> {
//	テーブル検索
	Optional<Category> findById(Integer categoryId);

}