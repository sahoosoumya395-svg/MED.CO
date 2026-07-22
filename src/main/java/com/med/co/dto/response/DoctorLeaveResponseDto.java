package com.med.co.dto.response;

import java.time.LocalDate;

import com.med.co.enums.LeaveStatus;
import com.med.co.enums.LeaveType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorLeaveResponseDto {

    private Long leaveId;

    private Long doctorId;

    private String doctorName;

    private LeaveType leaveType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String reason;

    private LeaveStatus status;

}