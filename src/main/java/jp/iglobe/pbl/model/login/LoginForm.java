package jp.iglobe.pbl.model.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

	@Data
	public class LoginForm {
	@NotBlank(message = "メールアドレスを入力してください。")
	@Email(message = "メールアドレスを正しく入力してください。")
	@Size(max = 100, message = "メールアドレスが長すぎます。")
	    
	private String mail;
	@NotBlank(message = "パスワードを入力してください。")
	@Size(max = 30, message = "パスワードが長すぎます。")
	private String password;
	}
