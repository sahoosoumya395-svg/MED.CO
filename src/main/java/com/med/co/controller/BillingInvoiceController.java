package com.med.co.controller;

import com.med.co.dto.request.BillingInvoiceRequestDto;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.BillingInvoiceResponseDto;
import com.med.co.service.BillingInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingInvoiceController {

	private final BillingInvoiceService billingInvoiceService;

	/**
	 * Create a new billing invoice
	 * POST /api/billing
	 */
	@PostMapping
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
	 * Get all billing invoices
	 * GET /api/billing
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<List<BillingInvoiceResponseDto>>> getAllBillingInvoices() {

		List<BillingInvoiceResponseDto> response = billingInvoiceService.getAllBillingInvoices();

		ApiResponse<List<BillingInvoiceResponseDto>> apiResponse = new ApiResponse<>(
				200,
				"All invoices retrieved successfully",
				response);

		return ResponseEntity.ok(apiResponse);
	}

	/**
	 * Get billing invoice by ID
	 * GET /api/billing/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BillingInvoiceResponseDto>> getBillingInvoiceById(
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
	 * DELETE /api/billing/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteBillingInvoice(@PathVariable Long id) {

		billingInvoiceService.deleteBillingInvoice(id);

		ApiResponse<Void> apiResponse = new ApiResponse<>(
				200,
				"Invoice deleted successfully",
				null);

		return ResponseEntity.ok(apiResponse);
	}
}
