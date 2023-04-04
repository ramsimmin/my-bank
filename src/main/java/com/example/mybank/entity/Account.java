package com.example.mybank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    private String id;

    @Column(nullable = false)
    @Min(0)
    private Double balance;

    @Column(nullable = false)
    private String currency;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;

}
