package jp.iglobe.pbl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "account_id")
    private Integer account_id;

    private String name;

    private String mail;

    private String password;

    private Integer authority;
}
