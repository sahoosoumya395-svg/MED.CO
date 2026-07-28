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
public class DoctorConsultationDto {

	@NotBlank(message = "Doctor name is required")
	private String doctorName;

	@NotBlank(message = "Specialization is required")
	private String specialization;

	@NotNull(message = "Doctor fee is required")
	@Positive(message = "Doctor fee must be greater than 0")
	private BigDecimal doctorFee;
}
