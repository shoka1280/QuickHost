package com.Project.QuickHost.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-M-d")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-M-d")
    private LocalDate endDate;
    private BigDecimal surgeFactor;
    private BigDecimal price;
    private boolean closed;
}
