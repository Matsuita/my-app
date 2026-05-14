package jp.iglobe.pbl.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
	private Integer saleId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate saleDate;
	private Integer accountId;
	private Integer categoryId;
	private String tradeName;
	private Integer unitPrice;
	private Integer saleNumber;
	private String note;
}
