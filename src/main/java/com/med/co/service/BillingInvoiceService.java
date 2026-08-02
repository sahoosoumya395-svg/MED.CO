package com.med.co.service;

import com.med.co.dto.request.BillingInvoiceRequestDto;
import com.med.co.dto.request.DateRangeBillingRequestDto;
import com.med.co.dto.response.BillingInvoiceResponseDto;
import com.med.co.dto.response.DateRangeBillingSummaryDto;
import com.med.co.dto.response.MonthlyBillingSummaryDto;

import java.util.List;

public interface BillingInvoiceService {

	BillingInvoiceResponseDto createBillingInvoice(BillingInvoiceRequestDto requestDto);

	BillingInvoiceResponseDto getBillingInvoiceById(Long invoiceId);

	BillingInvoiceResponseDto getBillingInvoiceByInvoiceNumber(String invoiceNumber);

	java.util.List<BillingInvoiceResponseDto> getBillingInvoicesByDate(java.time.LocalDate invoiceDate);

	List<BillingInvoiceResponseDto> getAllBillingInvoices();

	MonthlyBillingSummaryDto getCurrentMonthBillingSummary();

	DateRangeBillingSummaryDto getBillingSummaryByDateRange(DateRangeBillingRequestDto requestDto);

	void deleteBillingInvoice(Long invoiceId);
}
