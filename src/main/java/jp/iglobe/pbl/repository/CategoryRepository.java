package jp.iglobe.pbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.iglobe.pbl.model.category.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Integer> {

}