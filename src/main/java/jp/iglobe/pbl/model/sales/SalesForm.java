package jp.iglobe.pbl.model.sales;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SalesForm {
	
    private Integer saleId;
    
    @NotBlank(message = "販売日を入力してください。")
    private String saleDate;
    
    @NotNull(message = "担当を選択してください。")
    private Integer accountId;
    
    @NotNull(message = "商品カテゴリーを選択してください。")
    private Integer categoryId;
    
    @NotBlank(message = "商品名を入力してください。")
    private String tradeName;
    
    @NotNull(message = "単価を入力してください。")
    private Integer unitPrice;
    
    @NotNull(message = "個数を入力してください。")
    private Integer saleNumber;
    
    private String note;
    private String authority;
}
