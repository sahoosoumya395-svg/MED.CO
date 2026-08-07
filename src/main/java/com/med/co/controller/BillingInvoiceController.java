package com.med.co.controller;

import com.med.co.dto.request.BillingInvoiceRequestDto;
import com.med.co.dto.request.DateRangeBillingRequestDto;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.BillingInvoiceResponseDto;
import com.med.co.dto.response.DateRangeBillingSummaryDto;
import com.med.co.dto.response.MonthlyBillingSummaryDto;
import com.med.co.service.BillingInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingInvoiceController {

	private final BillingInvoiceService billingInvoiceService;

	/**
	 * Create a new billing invoice
	 * POST /api/billing/create/invoice
	 */
	@PostMapping("/create/invoice")
	public ResponseEntity<ApiResponse<BillingInvoiceResponseDto>> createBillingInvoice(
			@Valid @RequestBody BillingInvoiceRequestDto requestDto) {

		BillingInvoiceResponseDto response = billingInvoiceService.createBillingInvoice(requestDto);

		ApiResponse<BillingInvoiceResponseDto> apiResponse = new ApiResponse<>(
				201,
				"Invoice generated successfully",
				response);

		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	/**
	 * Calculate current month billing summary (1st day of month to today)
	 * GET /api/billing/calculate/current-month
	 */
	@GetMapping("/calculate/current-month")
	public ResponseEntity<ApiResponse<MonthlyBillingSummaryDto>> getCurrentMonthBillingSummary() {

		MonthlyBillingSummaryDto response = billingInvoiceService.getCurrentMonthBillingSummary();

		ApiResponse<MonthlyBillingSummaryDto> apiResponse = new ApiResponse<>(
				200,
				"Current month billing summary calculated successfully",
				response);

		return ResponseEntity.ok(apiResponse);
	}

	/**
	 * Calculate total billing payments between two dates
	 * POST /api/billing/calculate/by-date-range
	 */
	@PostMapping("/calculate/by-date-range")
	public ResponseEntity<ApiResponse<DateRangeBillingSummaryDto>> getBillingSummaryByDateRange(
			@Valid @RequestBody DateRangeBillingRequestDto requestDto) {

		DateRangeBillingSummaryDto response = billingInvoiceService.getBillingSummaryByDateRange(requestDto);

		ApiResponse<DateRangeBillingSummaryDto> apiResponse = new ApiResponse<>(
				200,
				"Date range billing summary calculated successfully",
				response);

		return ResponseEntity.ok(apiResponse);
	}

	/**
	 * Get all billing invoices
	 * GET /api/billing/all-invoices
	 */
	@GetMapping("/all-invoices")
	public ResponseEntity<ApiResponse<List<BillingInvoiceResponseDto>>> getAllBillingInvoices() {

		List<BillingInvoiceResponseDto> response = billingInvoiceService.getAllBillingInvoices();

		ApiResponse<List<BillingInvoiceResponseDto>> apiResponse = new ApiResponse<>(
				200,
				"All invoices retrieved successfully",
				response);

		return ResponseEntity.ok(apiResponse);
	}

	/**
	 * Get billing invoice by invoice number
	 * GET /api/billing/show/using-invoiceNumber
	 */
	@GetMapping("/show/using-invoiceNumber")
	public ResponseEntity<ApiResponse<BillingInvoiceResponseDto>> getBillingInvoiceByInvoiceNumber(
			@RequestParam String invoiceNumber) {

		BillingInvoiceResponseDto response = billingInvoiceService.getBillingInvoiceByInvoiceNumber(invoiceNumber);

		ApiResponse<BillingInvoiceResponseDto> apiResponse = new ApiResponse<>(
				200,
				"Invoice retrieved successfully",
				response);

		return ResponseEntity.ok(apiResponse);
	}

	/**
	 * Get billing invoices by date
	 * GET /api/billing/show/using-invoiceDate
	 */
	@GetMapping("/show/using-invoiceDate")
	public ResponseEntity<ApiResponse<List<BillingInvoiceResponseDto>>> getBillingInvoicesByDate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invoiceDate) {

		List<BillingInvoiceResponseDto> response = billingInvoiceService.getBillingInvoicesByDate(invoiceDate);

		String message = "Invoices retrieved successfully";
		if (response == null || response.isEmpty()) {
			message = "No billing data found for the requested date";
		}

		ApiResponse<List<BillingInvoiceResponseDto>> apiResponse = new ApiResponse<>(
				200,
				message,
				response);

		return ResponseEntity.ok(apiResponse);
	}

	/**
	 * Get billing invoice by ID
	 * GET /api/billing/show-bill/id-wise/{id}
	 */
	@GetMapping("/show-bill/id-wise/{id}")
	public ResponseEntity<ApiResponse<BillingInvoiceResponseDto>> getBillingInvoiceById(
			@Positive(message = "Invoice ID must be greater than 0")
			@PathVariable Long id) {

		BillingInvoiceResponseDto response = billingInvoiceService.getBillingInvoiceById(id);

		ApiResponse<BillingInvoiceResponseDto> apiResponse = new ApiResponse<>(
				200,
				"Invoice retrieved successfully",
				response);

		return ResponseEntity.ok(apiResponse);
	}

	/**
	 * Delete billing invoice by ID
	 * DELETE /api/billing/delete/id-wise/{id}
	 */
	@DeleteMapping("/delete/id-wise/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteBillingInvoice(@PathVariable Long id) {

		billingInvoiceService.deleteBillingInvoice(id);

		ApiResponse<Void> apiResponse = new ApiResponse<>(
				200,
				"Invoice deleted successfully",
				null);

		return ResponseEntity.ok(apiResponse);
	}
}
