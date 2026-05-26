package jp.iglobe.pbl.model.account;

import lombok.Data;

@Data

public class SearchAccount {

	private Integer accountId;
	private String name;
	private String mail;
	private String password;
	private Integer salesAuthority;
	private Integer accountAuthority;

}
