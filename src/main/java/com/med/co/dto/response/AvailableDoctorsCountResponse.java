package com.med.co.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableDoctorsCountResponse {
    private LocalDate date;
    private int availableCount;
    private int totalScheduledRecurring;    // Total scheduled via recurring patterns
    private int totalScheduledSpecific;     // Total scheduled on this specific date
    private int totalOnLeaveApproved;       // Total on approved leave
    private String message;
}
