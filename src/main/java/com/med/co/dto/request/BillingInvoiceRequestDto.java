package com.med.co.dto.request;

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
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
	@Positive(message = "Patient ID must be greater than 0")
	private Long patientId;

	@NotBlank(message = "Patient name is required")
	@Size(min = 2, max = 100, message = "Patient name must be between 2 and 100 characters")
	@Pattern( regexp = "^[A-Za-z ]+$",
	    	  message = "Patient name must contain only letters and spaces"
	)
	private String patientName;

	@NotNull(message = "Invoice date is required")
	@PastOrPresent(message = "Invoice date cannot be in the future")
	private LocalDate invoiceDate;

	@Valid
	@NotNull(message = "Doctor consultation details are required")
	private DoctorConsultationDto doctorConsultation;

	@Valid
	@NotEmpty(message = "At least one medicine is required")
	@Size(max = 100, message = "Maximum 100 medicines are allowed per invoice")

	private List<MedicineDto> medicines;
}
