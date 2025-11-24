package com.lamarfishing.core.account.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "account_bank")
    private String bank;

    @Column(name = "account_bank_account")
    private String bankAccount;

    private Account(String bank, String bankAccount) {
        this.bank = bank;
        this.bankAccount = bankAccount;
    }

    public static Account create(String bank, String bankAccount) {
        return new Account(bank, bankAccount);
    }
}
