package jp.iglobe.pbl.model.account;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data

@Table(name = "accounts")
public class SearchAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Integer accountId;

	private String name;
	private String mail;

	@Column(name = "password")
	private String password;

	private Integer authority;

}
