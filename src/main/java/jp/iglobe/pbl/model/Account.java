package jp.iglobe.pbl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private Integer id;
    private String mail;
    private String password;

}
