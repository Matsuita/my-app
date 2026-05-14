package jp.iglobe.pbl.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;



@Data
public class AccountForm {
	@Id
	private Integer accountId;
	
	@NotBlank(message = "名前を入力してください")
    private String name;

	@NotBlank(message = "メールアドレスを入力してください")
    private String mail;

	@NotBlank(message = "パスワードを入力してください")
    private String password;
    
	@NotBlank(message = "確認のためのパスワードを入力してください")
    private String passwordConfirm;

	@NotNull(message = "権限を選択してください")
    private Integer authority;
	
    
}