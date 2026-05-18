package jp.iglobe.pbl.model.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    
//    private List<Integer> authList;
    @NotNull(message = "権限を選択してください")
    private Integer authority;
}