package com.med.co.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRegistrationRequest {

    private String firstName;
    private String middleName;
    private String lastName;

    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private String nationality;

    private String mobileNumber;
    private String alternateMobileNumber;
    private String email;
    private String password;

    private String address;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    private String medicalRegistrationNumber;
    private String qualification;
    private String specialization;
    private Integer experience;
    private Long departmentId;
    private String designation;
}