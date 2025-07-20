package com.Project.QuickHost.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

import java.util.Set;

@Entity
@Getter
@Setter
@Table (name="Bookings")
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id", nullable = false) // create a room, specify the hotel it belongs to
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Integer roomCount; // number of rooms booked for this booking

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // create a booking, specify the user it belongs to
    private User user;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;
    @Column(nullable = false)
    private LocalDate updatedAt;



    @Column(nullable = false)
    private LocalDate checkInDate;
    @Column(nullable = false)
    private LocalDate checkOutDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payement_id") // create a booking, specify the payment it belongs to
    private Payement payement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @ManyToMany
    @JoinTable(name="booing_guest", //use join column which is going to own the relationship
    joinColumns=@JoinColumn(name="booking_id"),
    inverseJoinColumns=@JoinColumn(name="guest_id"))
    private Set<Guest> guests;

}

