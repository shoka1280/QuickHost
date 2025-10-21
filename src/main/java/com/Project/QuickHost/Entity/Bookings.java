package com.Project.QuickHost.Entity;

import com.Project.QuickHost.Entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table (name="Bookings")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne  (fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id", nullable = false) // create a room, specify the hotel it belongs to
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Integer roomCount; // number of rooms booked for this booking

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // create a booking, specify the user it belongs to
    private User user;

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;



    @Column(nullable = false)
    private LocalDate checkInDate;
    @Column(nullable = false)
    private LocalDate checkOutDate;

//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "payement_id") // create a booking, specify the payment it belongs to
//    private Payement payement;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @ManyToMany
    @JoinTable(name="booing_guest", //use join column which is going to own the relationship
    joinColumns=@JoinColumn(name="booking_id"),
    inverseJoinColumns=@JoinColumn(name="guest_id"))
    private Set<Guest> guests;

    @Column(nullable=false,precision=10,scale=2)
    private BigDecimal amount; // Amount of the payment

    @Column(unique = true)
    private String stripePaymentsessionId;

}

