package jp.iglobe.pbl.model.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

	@Data
	public class LoginForm {
	@NotBlank(message = "メールアドレスが未入力です。")
	@Email(message = "メールアドレスを正しく入力してください。")
	@Size(max = 100, message = "メールアドレスが長すぎます。")
	    
	private String mail;
	@NotBlank(message = "パスワードが未入力です。")
	@Size(max = 30, message = "パスワードを正しく入力してください。")
	private String password;
	}
