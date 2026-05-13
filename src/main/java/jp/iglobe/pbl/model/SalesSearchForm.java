package jp.iglobe.pbl.model;

import lombok.Data;

@Data
public class SalesSearchForm {
	
    private String saleDateFrom;
    private String saleDateTo;
    private String accountId;
    private String categoryId;
    private String tradeName;
    private String note;
   
}