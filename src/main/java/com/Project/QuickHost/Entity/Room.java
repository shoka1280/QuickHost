package com.Project.QuickHost.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)//create a room ,specifiy
    @JsonIgnore
    private Hotel hotel;


    @Column(nullable=false)
    private String type;

    //pricing Strategy
    @Column(nullable=false,precision=10,scale=2)//price upto 10 lenghth , after decimal 2
    private BigDecimal basePrice;

    @Column(columnDefinition="TEXT[]")//storing url in array of text
    private String[] photos;
    @Column(columnDefinition="TEXT[]")
    private String[] amenities;
    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Integer totalCount;
    private Integer capacity;
}
