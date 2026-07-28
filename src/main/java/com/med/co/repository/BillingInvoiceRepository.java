package com.med.co.repository;

import com.med.co.entity.BillingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BillingInvoiceRepository extends JpaRepository<BillingInvoice, Long> {

	// Find invoice by invoice number
	Optional<BillingInvoice> findByInvoiceNumber(String invoiceNumber);

	// Find invoices by patient ID
	java.util.List<BillingInvoice> findByPatientId(Long patientId);

	// Find invoices by patient name
	java.util.List<BillingInvoice> findByPatientName(String patientName);

	// Find invoices by invoice date
	java.util.List<BillingInvoice> findByInvoiceDate(LocalDate invoiceDate);

	// Check if invoice number exists
	boolean existsByInvoiceNumber(String invoiceNumber);
}
