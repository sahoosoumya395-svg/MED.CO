package com.med.co.dto.request;


import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorUpdateRequest {


    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50,
            message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$",
            message = "First name should contain only alphabets")
    private String firstName;


    @Size(max = 50,
            message = "Middle name cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z ]*$",
            message = "Middle name should contain only alphabets")
    private String middleName;


    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50,
            message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$",
            message = "Last name should contain only alphabets")
    private String lastName;


    @NotBlank(message = "Gender is required")
    @Pattern(
        regexp = "^(Male|Female|Other)$",
        message = "Gender must be Male, Female or Other"
    )
    private String gender;


    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be a past date")
    private LocalDate dateOfBirth;


    @NotBlank(message = "Blood group is required")
    @Pattern(
        regexp = "^(A|B|AB|O)[+-]$",
        message = "Invalid blood group"
    )
    private String bloodGroup;


    @NotBlank(message = "Nationality is required")
    private String nationality;


    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Invalid mobile number"
    )
    private String mobileNumber;


    @Pattern(
        regexp = "^$|^[6-9]\\d{9}$",
        message = "Invalid alternate mobile number"
    )
    private String alternateMobileNumber;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;


    @NotBlank(message = "Address is required")
    @Size(max = 500,
            message = "Address cannot exceed 500 characters")
    private String address;


    @NotBlank(message = "City is required")
    private String city;


    @NotBlank(message = "State is required")
    private String state;


    @NotBlank(message = "Country is required")
    private String country;


    @NotBlank(message = "PIN code is required")
    @Pattern(
        regexp = "^[1-9][0-9]{5}$",
        message = "Invalid PIN code"
    )
    private String pinCode;


    @NotBlank(message = "Medical registration number is required")
    @Size(min = 5, max = 30,
            message = "Medical registration number must be between 5 and 30 characters")
    private String medicalRegistrationNumber;


    @NotBlank(message = "Qualification is required")
    private String qualification;


    @NotBlank(message = "Specialization is required")
    private String specialization;


    @NotNull(message = "Experience is required")
    @Min(value = 0,
            message = "Experience cannot be negative")
    @Max(value = 60,
            message = "Experience cannot exceed 60 years")
    private Integer experience;


    @NotNull(message = "Department is required")
    private Long departmentId;


    @NotBlank(message = "Designation is required")
    private String designation;

}