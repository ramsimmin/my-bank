package com.example.mybank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    private String id;

    @Column(name = "source_account_id", nullable = false)
    @Basic
    private String sourceAccountId;

    @Column(name = "target_account_id", nullable = false)
    @Basic
    private String targetAccountId;

    @ManyToOne
    @JoinColumn(name = "source_account_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Account targetAccount;

    private Double amount;

    private String currency;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;


}
