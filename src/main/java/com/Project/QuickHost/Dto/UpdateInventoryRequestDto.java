package com.Project.QuickHost.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Getter
@Setter
public class UpdateInventoryRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal surgeFactor;
    private BigDecimal price;
    private boolean closed;
}
