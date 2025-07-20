package com.Project.QuickHost.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Payement {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String transactionId; // Unique identifier for the transaction

    @Column(unique = true)
    private BigDecimal amount; // Amount of the payment

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // Status of the payment (e.g., PENDING, COMPLETED, FAILED)


}
