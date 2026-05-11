package com.Project.QuickHost.Dto;

import com.Project.QuickHost.Entity.Hotel;
import com.Project.QuickHost.Entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InventoryDto {

   private Long id;
    private LocalDate date;
    private Integer bookedCount;
    private Integer reservedCount;
    private Integer totalCount;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal surgeFactor;
    private BigDecimal price;


    private boolean closed;
}
