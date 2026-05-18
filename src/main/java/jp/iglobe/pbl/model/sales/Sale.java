package jp.iglobe.pbl.model.sales;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "sales")
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sale_id")
	private Integer saleId;
	
	@Column(name = "sale_date")
	private LocalDate saleDate;
	
	@Column(name = "account_id")
	private Integer accountId;
	
	@Column(name = "category_id")
	private Integer categoryId;
	
	@Column(name = "trade_name")
	private String tradeName;
	
	@Column(name = "unit_price")
	private Integer unitPrice;
	
	@Column(name = "sale_number")
	private Integer saleNumber;
	
	@Column(name = "note")
	private String note;
}
