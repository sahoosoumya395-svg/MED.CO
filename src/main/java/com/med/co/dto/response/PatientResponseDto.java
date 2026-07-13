package com.med.co.dto.response;

import java.time.LocalDate;

import com.med.co.enums.Enums.BloodGroup;
import com.med.co.enums.Enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {

    private Long patientId;

    private String firstName;
    private String middleName;
    private String lastName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private BloodGroup bloodGroup;

    private String nationality;

    private String profilePhoto;

    private String mobileNumber;

    private String alternateNumber;

    private String address;

    private String city;

    private String state;

    private String country;

    private String pinCode;

    private Double height;

    private Double weight;

    private String visionStatus;

    private Double bodyTemperature;

    private String hearingStatus;
}