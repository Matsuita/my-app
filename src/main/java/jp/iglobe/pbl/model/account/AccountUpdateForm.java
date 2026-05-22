package jp.iglobe.pbl.model.account;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountUpdateForm {

	private Integer accountId;

	@NotBlank(message = "氏名を入力して下さい。")
	@Size(max = 20, message = "氏名が長すぎます。")
	private String name;

	@NotBlank(message = "メールアドレスを入力して下さい。")
	@Pattern(regexp = "^$|[a-zA-Z0-9_+-]+(\\.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}$", message = "メールアドレスの形式が誤っています。")
	@Size(max = 100, message = "メールアドレスが長すぎます。")
	private String mail;

	@NotBlank(message = "パスワードを入力して下さい。")
	@Size(max = 30, message = "パスワードが長すぎます。")
	@Pattern(regexp = "^$|(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "パスワードは8〜20文字で、大文字・小文字・数字をそれぞれ1つ以上含めてください")
	private String password;
	@NotBlank(message = "パスワード（確認）を入力して下さい。")
	@Transient
	private String passwordConfirm;

	@NotNull(message = "売上権限を選択して下さい。")
	private Integer salesAuthority;

	@NotNull(message = "アカウント権限を選択して下さい。")
	private Integer accountsAuthority;
	//	private Integer authority;

	@AssertTrue(message = "パスワードが一致していません。")
	public boolean isPasswordValid() {
		if (password == null || passwordConfirm == null) {
			return false;
		}
		return password.equals(passwordConfirm);
	}
}