package com.Project.QuickHost.Entity;

import com.Project.QuickHost.Entity.enums.PaymentStatus;
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

    @Column(nullable=false,precision=10,scale=2)
    private BigDecimal amount; // Amount of the payment

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // Status of the payment (e.g., PENDING, COMPLETED, FAILED)

        @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id") // create a booking, specify the payment it belongs to
    private Bookings booking;
}
