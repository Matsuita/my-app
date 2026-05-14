package jp.iglobe.pbl.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "sale_date")
	@NotNull(message = "期限を入力してください")
	private LocalDate saleDate;
	
	@Column(name = "account_id")
	private Integer accountId;
	
	@Column(name = "category_id")
	private Integer categoryId;
	
	@Column(name = "trade_name")
	@NotBlank(message = "商品名を入力してください")
	private String tradeName;
	
	@Column(name = "unit_price")
	@NotBlank(message = "単価を入力してください")
	private Integer unitPrice;
	
	@Column(name = "sale_number")
	@NotBlank(message = "個数を入力してください")
	private Integer saleNumber;
	
	@Column(name = "note")
	private String note;
}
