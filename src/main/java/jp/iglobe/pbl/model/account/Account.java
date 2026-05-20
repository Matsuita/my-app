package jp.iglobe.pbl.model.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    private String name;

    private String mail;

    private String password;
    
    private Integer salesAuthority;
    
    private Integer accountsAuthority;
    
    private boolean isActive;

//    private Integer authority;
    
    @Transient
    private String passwordConfirm;
}
