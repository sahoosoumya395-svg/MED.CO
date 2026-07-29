package com.med.co.entity;

import java.time.LocalDate;

import com.med.co.enums.Enums.BloodGroup;
import com.med.co.enums.Enums.Gender;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Blood group is required")
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    private String profilePhoto;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Mobile number must be exactly 10 digits"
    )
    @Column(nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Pattern(
        regexp = "^$|^[6-9]\\d{9}$",
        message = "Alternate number must be exactly 10 digits"
    )
    private String alternateNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
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

    @Positive(message = "Height must be greater than 0")
    private Double height;

    @Positive(message = "Weight must be greater than 0")
    private Double weight;

    private String visionStatus;

    @Positive(message = "Body temperature must be greater than 0")
    private Double bodyTemperature;

    private String hearingStatus;

    @Column(name = "mrn_no", unique = true, nullable = false)
    private String mrnNo;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserRole userrole;
}