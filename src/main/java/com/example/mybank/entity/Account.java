package com.example.mybank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

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
