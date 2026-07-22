package com.med.co.dto.request;

import com.med.co.enums.LeaveStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveStatusRequestDto {

    @NotNull
    private LeaveStatus status;

}