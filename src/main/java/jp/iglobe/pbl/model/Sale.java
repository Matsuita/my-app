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
import jakarta.validation.constraints.Size;

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
	@NotNull(message = "販売日を入力してください。")
	private LocalDate saleDate;
	
	@Column(name = "account_id")
	@NotNull(message = "担当が未選択です。")
	private Integer accountId;
	
	@Column(name = "category_id")
	@NotNull(message = "商品カテゴリーが未選択です。")
	private Integer categoryId;
	
	@Column(name = "trade_name")
	@NotBlank(message = "商品名を入力してください。")
	@Size(max = 30, message = "商品名が長すぎます。")
	private String tradeName;
	
	@Column(name = "unit_price")
	@NotNull(message = "単価を入力してください。")
	private Integer unitPrice;
	
	@Column(name = "sale_number")
	@NotNull(message = "個数を入力してください。")
	private Integer saleNumber;
	
	@Column(name = "note")
	@Size(max = 100, message = "備考が長すぎます。")
	private String note;
}
