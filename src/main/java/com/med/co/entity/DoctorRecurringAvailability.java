package com.med.co.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_recurring_availability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRecurringAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recurringAvailabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecurrenceType recurrenceType;

    // For MONTHLY_BY_DAY: 1-31 (day of month)
    @Column
    private Integer dayOfMonth;

    // For WEEKLY_BY_DAY: 1=MON, 2=TUE, ..., 7=SUN
    @Column
    private Integer dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    // Optional: validity window
    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    public enum RecurrenceType {
        MONTHLY_BY_DAY,
        WEEKLY_BY_DAY,
        DAILY
    }
}
