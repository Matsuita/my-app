package jp.iglobe.pbl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class Category {

    @Id
    private Integer categoryId;

    private String categoryName;
}
