package com.Project.QuickHost.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Long numberOfBookings;
    private BigDecimal totalRevenue;
    private BigDecimal avgRevenue;
}
