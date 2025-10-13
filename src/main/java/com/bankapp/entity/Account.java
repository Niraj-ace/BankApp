package com.bankapp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountHolderName; // Added field for the account holder's name

    private double balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Ensure every account is linked to a user
    private User user;
}
