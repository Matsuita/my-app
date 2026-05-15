package jp.iglobe.pbl.model.account;

import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountSearchForm {

	@Size(max = 20, message = "氏名の指定が長すぎます。")
	private String name;
	@Size(max = 100, message = "メールアドレスの指定が長すぎます。")
	private String mail;
	private Integer authority;

}
