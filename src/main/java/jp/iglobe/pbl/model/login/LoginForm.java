package jp.iglobe.pbl.model.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

	@Data
	public class LoginForm {
	@NotBlank(message = "メールアドレスが未入力です。")
	@Size(max = 100, message = "メールアドレスが長すぎます。")
	@Pattern(regexp = "^$|[a-zA-Z0-9_+-]+(\\.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}$", message = "メールアドレスを正しく入力して下さい。")   
	private String mail;
	@NotBlank(message = "パスワードが未入力です。")
	@Size(max = 30, message = "パスワードを正しく入力してください。")
	private String password;
	}
