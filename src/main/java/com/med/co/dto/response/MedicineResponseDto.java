package com.med.co.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineResponseDto {

	private String medicineName;

	private Integer quantity;

	private BigDecimal unitPrice;

	private BigDecimal medicineTotal;
}
