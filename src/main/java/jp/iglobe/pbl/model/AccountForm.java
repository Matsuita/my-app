package jp.iglobe.pbl.model;

import lombok.Data;

@Data
public class AccountForm {

    private String name;

    private String mail;

    private String password;

    private String authority;
}