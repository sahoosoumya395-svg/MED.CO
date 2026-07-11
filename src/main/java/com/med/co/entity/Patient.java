package com.med.co.entity;

import java.time.LocalDate;

import com.med.co.enums.Enums.BloodGroup;
import com.med.co.enums.Enums.Gender;

import jakarta.persistence.*;
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

    private String firstName;
    private String middleName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
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

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserRole userrole;
}