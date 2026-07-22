package com.med.co.entity;

import java.time.LocalDate;

import com.med.co.enums.Enums.DoctorStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    private String bloodGroup;

    private String nationality;

    @Column(nullable = false, unique = true)
    private String mobileNumber;

    private String alternateMobileNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 500)
    private String address;

    private String city;

    private String state;

    private String country;

    private String pinCode;

    @Column(nullable = false, unique = true)
    private String medicalRegistrationNumber;

    private String qualification;

    private String specialization;

    private Integer experience;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    private String designation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DoctorStatus status;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<DoctorAvailability> availabilities;
}