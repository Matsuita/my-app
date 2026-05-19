package jp.iglobe.pbl.model.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountForm {

    @NotBlank(message = "氏名を入力して下さい。")
    @Size(max = 20, message = "氏名が長すぎます。")
    private String name;
    
    @Email(message = "メールアドレスを正しく入力して下さい。")
    @NotBlank(message = "メールアドレスを入力して下さい。")
    @Size(max = 100, message = "メールアドレスが長すぎます。") 
    // 101バイト以上をはじく
    private String mail;
    
    @NotBlank(message = "パスワードが未入力です。")
    @Size(max = 30 ,message = "パスワードが長すぎます。")
    private String password;
    
    @NotBlank(message = "パスワード（確認）が未入力です。")
    private String passwordConfirm;
    
    @NotNull(message = "売上権限を選択して下さい。")
    private Integer salesAuthority;
    
    @NotNull(message = "アカウント権限を選択して下さい。")
    private Integer accountsAuthority;
       
    
}