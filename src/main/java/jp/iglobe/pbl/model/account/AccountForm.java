package jp.iglobe.pbl.model.account;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AccountForm {

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