package com.Project.QuickHost.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(nullable  = false)
    private String name;
    private String city;
    @Column(columnDefinition="TEXT[]")//storing url in array of text
    private String[] photos;
    @Column(columnDefinition="TEXT[]")
    private String[] amenities;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Embedded//contactInfo will be embedded in hotel table(same)
    private HotelContactInfo contactInfo;//will embed contact_info_phonenumber,contact_info_address
    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "hotel",fetch = FetchType.LAZY)
    private List<Room> room;
}
