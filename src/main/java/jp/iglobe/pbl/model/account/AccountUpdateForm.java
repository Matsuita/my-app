package jp.iglobe.pbl.model.account;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountUpdateForm {

	private Integer accountId;

	@NotBlank(message = "氏名を入力して下さい。")
	@Size(max = 20, message = "氏名が長すぎます。")
	private String name;

	@NotBlank(message = "メールアドレスを入力して下さい。")
	@Size(max = 100, message = "メールアドレスが長すぎます。")
	@Email(message = "メールアドレスの形式が誤っています。")
	private String mail;

	@NotBlank(message = "パスワードを入力して下さい。")
	@Size(max = 30, message = "パスワードが長すぎます。")
	private String password;
	@NotBlank(message = "パスワード（確認）を入力して下さい。")
	@Transient
	private String passwordConfirm;
	private Integer authority;

	@AssertTrue(message = "パスワードが一致していません。")
	@AssertTrue(message = "パスワードが一致していません。")
	public boolean isPasswordValid() {
	    if (password == null || passwordConfirm == null) {
	        return false;
	    }
	    return password.equals(passwordConfirm);
	}
}