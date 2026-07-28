package com.med.co.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDto {

	@NotBlank(message = "Medicine name is required")
	private String medicineName;

	@NotNull(message = "Quantity is required")
	@Positive(message = "Quantity must be greater than 0")
	private Integer quantity;

	@NotNull(message = "Unit price is required")
	@Positive(message = "Unit price must be greater than 0")
	private BigDecimal unitPrice;
}
