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

	// Find invoices between start date and end date
	java.util.List<BillingInvoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);

	// Calculate total amount sum between start date and end date
	@org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM BillingInvoice b WHERE b.invoiceDate BETWEEN :fromDate AND :toDate")
	java.math.BigDecimal calculateTotalAmountBetweenDates(
			@org.springframework.data.repository.query.Param("fromDate") LocalDate fromDate,
			@org.springframework.data.repository.query.Param("toDate") LocalDate toDate);

	// Check if invoice number exists
	boolean existsByInvoiceNumber(String invoiceNumber);
}
