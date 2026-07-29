package com.med.co.dto.request;

import java.time.LocalDate;

import com.med.co.enums.LeaveType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DoctorLeaveRequestDto {

    @NotNull(message = "Doctor Id is required")
    private Long doctorId;

    @NotNull(message = "Leave Type is required")
    private LeaveType leaveType;

    @NotNull(message = "From Date is required")
    @FutureOrPresent(message = "From Date cannot be in the past")
    private LocalDate fromDate;

    @NotNull(message = "To Date is required")
    @FutureOrPresent(message = "To Date cannot be in the past")
    private LocalDate toDate;

    @NotBlank(message = "Reason is required")
    @Size(min = 5, max = 500, message = "Reason must be between 5 and 500 characters")
    private String reason;

}