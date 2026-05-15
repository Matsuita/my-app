package jp.iglobe.pbl.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SalesSearchForm {

    // 販売日（開始）

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDateFrom;

    // 販売日（終了）

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDateTo;

    // 担当
    private Integer accountId;

    // 商品カテゴリー
    private Integer categoryId;

    // 商品名
    private String tradeName;

    // 備考
    private String note;
}