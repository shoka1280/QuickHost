package com.Project.QuickHost.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class HotelMinPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "hotel_id", nullable = false) // create a room, specify the hotel it belongs to
    private Hotel hotel;

    @Column(nullable = false)
    private LocalDate date;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable=false,precision=10,scale=2)//min price for this particular hotel on particular date
    private BigDecimal price;
//    //getters and setters
//    public Long getId() {
//        return id;
//    }
   public HotelMinPrice(Hotel hotel ,LocalDate date){
        this.hotel=hotel;
        this.date=date;
    }

}
