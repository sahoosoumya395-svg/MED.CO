package com.med.co.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyBillingSummaryDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private long totalInvoicesCount;
    private BigDecimal totalAmount;
    private List<BillingInvoiceResponseDto> invoices;
}
