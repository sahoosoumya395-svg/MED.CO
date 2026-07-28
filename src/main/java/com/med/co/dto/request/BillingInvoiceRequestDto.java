package com.med.co.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingInvoiceRequestDto {

	@NotNull(message = "Patient ID is required")
	private Long patientId;

	@NotBlank(message = "Patient name is required")
	private String patientName;

	@NotNull(message = "Invoice date is required")
	private LocalDate invoiceDate;

	@Valid
	@NotNull(message = "Doctor consultation details are required")
	private DoctorConsultationDto doctorConsultation;

	@Valid
	@NotEmpty(message = "At least one medicine is required")
	private List<MedicineDto> medicines;
}
