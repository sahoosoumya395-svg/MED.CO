package com.med.co.dto.request;

import java.time.LocalDate;

import com.med.co.enums.Enums.BloodGroup;
import com.med.co.enums.Enums.Gender;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegistrationRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    private String profilePhoto;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Mobile number must be exactly 10 digits"
    )
    private String mobileNumber;

    @Pattern(
        regexp = "^$|^[6-9]\\d{9}$",
        message = "Alternate number must be exactly 10 digits"
    )
    private String alternateNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
        message = "Email must be a valid Gmail address"
    )
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Pin code is required")
    @Pattern(
        regexp = "^[1-9][0-9]{5}$",
        message = "Pin code must be 6 digits"
    )
    private String pinCode;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be greater than 0")
    private Double height;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than 0")
    private Double weight;

    private String visionStatus;

    @NotNull(message = "Body temperature is required")
    @Positive(message = "Body temperature must be greater than 0")
    private Double bodyTemperature;

    private String hearingStatus;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}