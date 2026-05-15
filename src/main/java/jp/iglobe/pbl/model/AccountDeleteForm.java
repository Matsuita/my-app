package jp.iglobe.pbl.model;

import lombok.Data;

@Data
public class AccountDeleteForm {
	    private Integer accountId;
	    private String name;
	    private String mail;
	    private String password;
	    private String passwordConfirm;
	    private Integer authority;
	}
