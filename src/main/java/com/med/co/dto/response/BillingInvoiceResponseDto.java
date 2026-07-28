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
public class BillingInvoiceResponseDto {

	private Long id;

	private String invoiceNumber;

	private Long patientId;

	private String patientName;

	private LocalDate invoiceDate;

	private DoctorConsultationResponseDto doctorConsultation;

	private List<MedicineResponseDto> medicines;

	private BigDecimal doctorSubtotal;

	private BigDecimal medicineSubtotal;

	private BigDecimal serviceCharge;

	private BigDecimal tax;

	private BigDecimal totalAmount;
}
