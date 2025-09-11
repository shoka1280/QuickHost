package com.Project.QuickHost.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        name="unique_hotelid_roomid_data",
                columnNames ={"hotel_id","room_id","date"} )//ensuring that each room-hote-data combo remain unique
        })
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//hotl id ,room id,unique data

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "hotel_id", nullable = false) // create a room, specify the hotel it belongs to
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) // create a room, specify the room it belongs to
    private Room room;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false,columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount; // number of rooms booked for this date

    @Column(nullable = false,columnDefinition = "INTEGER DEFAULT 0")
    private Integer reservedCount; //reserviing for 10 min while booking is in progess




    @Column(nullable = false)
    private Integer totalCount;


    private LocalDate createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    precision is the total number of digits allowed in the number (both before and after the decimal point).
//    scale is the number of digits allowed after the decimal point.
    @Column(nullable = false,precision = 5,scale=2)
    private BigDecimal surgeFactor; // price for the room on this date

    @Column(nullable=false,precision=10,scale=2)//price of invetory on that particular day
    private BigDecimal price;

    @Column(nullable = false)
    private String city;//de normalizing data,not using join[since city wont change]

    @Column(nullable = false)
    private boolean closed;




}
