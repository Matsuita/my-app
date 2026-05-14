package jp.iglobe.pbl.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id

    @Column(name = "account_id")
    private Integer id;

    private String name;

    private String mail;

    private String password;

    private Integer authority;
}
