package com.med.co.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "billing_invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingInvoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id")
	private Long invoiceId;

	@Column(name = "patient_id", nullable = false)
	private Long patientId;

	@Column(name = "patient_name", nullable = false, length = 100)
	private String patientName;

	@Column(name = "invoice_date", nullable = false)
	private LocalDate invoiceDate;

	@Column(name = "invoice_number", nullable = false, unique = true, length = 50)
	private String invoiceNumber;

	@Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
