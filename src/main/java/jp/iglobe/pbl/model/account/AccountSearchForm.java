package jp.iglobe.pbl.model.account;

import java.util.List;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountSearchForm {

	@Size(max = 20, message = "氏名の指定が長すぎます。")
	private String name;
	@Size(max = 100, message = "メールアドレスの指定が長すぎます。")
	@Pattern(regexp = "^$|[a-zA-Z0-9_+-]+(\\.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}$", 
	message = "メールアドレスを正しく入力して下さい。")
	private String mail;
	
    private List<Integer> salesAuthority;
    
    private List<Integer> accountsAuthority;


}
