package com.med.co.serviceimpl;

import com.med.co.dto.request.BillingInvoiceRequestDto;
import com.med.co.dto.request.MedicineDto;
import com.med.co.dto.response.BillingInvoiceResponseDto;
import com.med.co.dto.response.DoctorConsultationResponseDto;
import com.med.co.dto.response.MedicineResponseDto;
import com.med.co.entity.BillingInvoice;
import com.med.co.exception.ResourceNotFoundException;
import com.med.co.repository.BillingInvoiceRepository;
import com.med.co.service.BillingInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingInvoiceServiceImpl implements BillingInvoiceService {

	private final BillingInvoiceRepository billingInvoiceRepository;
	private static final BigDecimal SERVICE_CHARGE = new BigDecimal("10.00");
	private static final BigDecimal TAX_PERCENTAGE = new BigDecimal("5");
	private static final BigDecimal HUNDRED = new BigDecimal("100");

	@Override
	public BillingInvoiceResponseDto createBillingInvoice(BillingInvoiceRequestDto requestDto) {
		// Calculate medicineTotal for each medicine
		BigDecimal medicineSubtotal = BigDecimal.ZERO;
		List<MedicineResponseDto> medicineResponses = requestDto.getMedicines().stream()
				.map(medicine -> {
					BigDecimal medicineTotal = medicine.getUnitPrice()
							.multiply(new BigDecimal(medicine.getQuantity()))
							.setScale(2, RoundingMode.HALF_UP);
					return MedicineResponseDto.builder()
							.medicineName(medicine.getMedicineName())
							.quantity(medicine.getQuantity())
							.unitPrice(medicine.getUnitPrice())
							.medicineTotal(medicineTotal)
							.build();
				})
				.collect(Collectors.toList());

		// Calculate medicineSubtotal
		for (MedicineResponseDto medicine : medicineResponses) {
			medicineSubtotal = medicineSubtotal.add(medicine.getMedicineTotal());
		}
		medicineSubtotal = medicineSubtotal.setScale(2, RoundingMode.HALF_UP);

		// Doctor subtotal
		BigDecimal doctorSubtotal = requestDto.getDoctorConsultation().getDoctorFee()
				.setScale(2, RoundingMode.HALF_UP);

		// Calculate tax (5% of doctorSubtotal + medicineSubtotal + serviceCharge)
		BigDecimal subtotalBeforeTax = doctorSubtotal.add(medicineSubtotal).add(SERVICE_CHARGE);
		BigDecimal tax = subtotalBeforeTax
				.multiply(TAX_PERCENTAGE)
				.divide(HUNDRED, 2, RoundingMode.HALF_UP);

		// Calculate total amount
		BigDecimal totalAmount = doctorSubtotal.add(medicineSubtotal)
				.add(SERVICE_CHARGE).add(tax);

		// Generate invoice number
		String invoiceNumber = generateInvoiceNumber(requestDto.getInvoiceDate());

		// Save to database
		BillingInvoice billingInvoice = BillingInvoice.builder()
				.patientId(requestDto.getPatientId())
				.patientName(requestDto.getPatientName())
				.invoiceDate(requestDto.getInvoiceDate())
				.invoiceNumber(invoiceNumber)
				.totalAmount(totalAmount)
				.build();

		BillingInvoice savedInvoice = billingInvoiceRepository.save(billingInvoice);

		// Build response
		DoctorConsultationResponseDto doctorConsultationResponse = DoctorConsultationResponseDto.builder()
				.doctorName(requestDto.getDoctorConsultation().getDoctorName())
				.specialization(requestDto.getDoctorConsultation().getSpecialization())
				.doctorFee(requestDto.getDoctorConsultation().getDoctorFee())
				.build();

		return BillingInvoiceResponseDto.builder()
				.id(savedInvoice.getInvoiceId())
				.invoiceNumber(savedInvoice.getInvoiceNumber())
				.patientId(savedInvoice.getPatientId())
				.patientName(savedInvoice.getPatientName())
				.invoiceDate(savedInvoice.getInvoiceDate())
				.doctorConsultation(doctorConsultationResponse)
				.medicines(medicineResponses)
				.doctorSubtotal(doctorSubtotal)
				.medicineSubtotal(medicineSubtotal)
				.serviceCharge(SERVICE_CHARGE)
				.tax(tax)
				.totalAmount(totalAmount)
				.build();
	}

	@Override
	public BillingInvoiceResponseDto getBillingInvoiceById(Long invoiceId) {
		BillingInvoice billingInvoice = billingInvoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Billing invoice not found with id: " + invoiceId));

		// Reconstruct response (in a real scenario, you might want to store more details)
		return BillingInvoiceResponseDto.builder()
				.id(billingInvoice.getInvoiceId())
				.invoiceNumber(billingInvoice.getInvoiceNumber())
				.patientId(billingInvoice.getPatientId())
				.patientName(billingInvoice.getPatientName())
				.invoiceDate(billingInvoice.getInvoiceDate())
				.totalAmount(billingInvoice.getTotalAmount())
				.build();
	}

	@Override
	public List<BillingInvoiceResponseDto> getAllBillingInvoices() {
		List<BillingInvoice> invoices = billingInvoiceRepository.findAll();
		return invoices.stream()
				.map(invoice -> BillingInvoiceResponseDto.builder()
						.id(invoice.getInvoiceId())
						.invoiceNumber(invoice.getInvoiceNumber())
						.patientId(invoice.getPatientId())
						.patientName(invoice.getPatientName())
						.invoiceDate(invoice.getInvoiceDate())
						.totalAmount(invoice.getTotalAmount())
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public void deleteBillingInvoice(Long invoiceId) {
		BillingInvoice billingInvoice = billingInvoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Billing invoice not found with id: " + invoiceId));
		billingInvoiceRepository.delete(billingInvoice);
	}

	/**
	 * Generate invoice number in the format: INV-YYYYMMDD-0001
	 * This method gets the latest invoice number for the date and increments it
	 */
	private String generateInvoiceNumber(LocalDate invoiceDate) {
		String dateString = invoiceDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String prefix = "INV-" + dateString + "-";

		// Get all invoices for this date and find the next sequence number
		List<BillingInvoice> invoicesForDate = billingInvoiceRepository
				.findByInvoiceDate(invoiceDate);

		int nextSequence = invoicesForDate.size() + 1;
		String sequenceNumber = String.format("%04d", nextSequence);

		String invoiceNumber = prefix + sequenceNumber;

		// Double-check that this invoice number doesn't already exist
		// (In case of concurrent requests)
		while (billingInvoiceRepository.existsByInvoiceNumber(invoiceNumber)) {
			nextSequence++;
			sequenceNumber = String.format("%04d", nextSequence);
			invoiceNumber = prefix + sequenceNumber;
		}

		return invoiceNumber;
	}
}
