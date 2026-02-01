package com.bankservice.account;

import com.bankservice.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "계좌")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "사용자_id")
    private User user;

    @Column(name = "계좌번호", nullable = false)
    private String accountNumber;

    @Column(name = "잔액", nullable = false)
    private BigDecimal balance;

    protected Account() {}

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
