package jp.iglobe.pbl.model.sales;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SalesCreateForm {
	
	private Integer saleId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "販売日を入力してください。")
	private LocalDate saleDate;
	
	@NotNull(message = "担当が未選択です。")
	private Integer accountId;

	@NotNull(message = "商品カテゴリーが未選択です。")
	private Integer categoryId;

	@NotBlank(message = "商品名を入力してください。")
	@Size(max = 100, message = "商品名が長すぎます。")
	private String tradeName;

	@NotBlank(message = "単価を入力してください。")
	@Size(max = 10, message = "単価が長すぎます。")
	@Pattern(regexp = "(^$)|^[0-9]+$", message = "単価を正しく入力してください。")
	private String unitPrice;

	@NotBlank(message = "個数を入力してください。")
	@Size(max = 10, message = "個数が長すぎます。")
	@Pattern(regexp = "(^$)|^([1-9][0-9]*)$", message = "個数を正しく入力してください。")
	private String saleNumber;

	@Size(max = 400, message = "備考が長すぎます。")
	private String note;
}
