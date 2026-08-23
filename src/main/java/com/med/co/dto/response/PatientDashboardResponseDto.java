package com.med.co.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDashboardResponseDto {

    private String patientName;

    private Stats stats;

    private HealthOverview healthOverview;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stats {

        private long totalAppointments;

        private long upcomingAppointments;

        private long doctorsConsulted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthOverview {

        private String bloodGroup;

        private String height;

        private String weight;
    }
}