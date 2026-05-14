package jp.iglobe.pbl.model;

import lombok.Data;

@Data
public class AccountSearchForm {
	private String name;
	private String mail;
	private Integer authority;

}
