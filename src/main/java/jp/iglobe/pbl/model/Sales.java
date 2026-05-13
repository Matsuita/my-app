package jp.iglobe.pbl.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "sales")
public class Sales {

    // ID
    @Id
    private Integer saleId;
    private Date saleDate;
    private Integer accountId;
    private Integer categoryId;
    private String tradeName;
    private Integer unitPrice;
    private Integer saleNumber;
    private String note;
}