package jp.iglobe.pbl.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "accounts")
@Data
public class AccountForm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer account_id;

	@NotBlank(message = "名前を入力してください")
	private String name;

	@Email(message = "メールアドレスの形式が正しくありません")
	@NotBlank(message = "メールアドレスを入力してください")
	private String mail;

	@NotBlank(message = "パスワードを入力してください")
	private String password;

	@NotBlank(message = "確認のためのパスワードを入力してください")
	private String passwordConfirm;

	private List<Integer> authList;
	private Integer authority;

}