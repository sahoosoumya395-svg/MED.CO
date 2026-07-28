package com.med.co.service;

import com.med.co.dto.request.BillingInvoiceRequestDto;
import com.med.co.dto.response.BillingInvoiceResponseDto;

import java.util.List;

public interface BillingInvoiceService {

	BillingInvoiceResponseDto createBillingInvoice(BillingInvoiceRequestDto requestDto);

	BillingInvoiceResponseDto getBillingInvoiceById(Long invoiceId);

	BillingInvoiceResponseDto getBillingInvoiceByInvoiceNumber(String invoiceNumber);

	java.util.List<BillingInvoiceResponseDto> getBillingInvoicesByDate(java.time.LocalDate invoiceDate);

	List<BillingInvoiceResponseDto> getAllBillingInvoices();

	void deleteBillingInvoice(Long invoiceId);
}
