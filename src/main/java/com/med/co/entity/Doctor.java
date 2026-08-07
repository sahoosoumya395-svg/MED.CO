package com.med.co.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.med.co.enums.Enums.DoctorStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	// Personal Details

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(length = 50)
	private String middleName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@Column(nullable = false, length = 10)
	private String gender;

	@Column(nullable = false)
	private LocalDate dateOfBirth;

	@Column(nullable = false, length = 5)
	private String bloodGroup;

	@Column(nullable = false, length = 50)
	private String nationality;
	
	@Column(name = "registration_code", nullable = false)
	private Integer registrationCode;

	// Contact Details

	@Column(nullable = false, unique = true, length = 10)
	private String mobileNumber;

	@Column(length = 10)
	private String alternateMobileNumber;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false, length = 500)
	private String address;

	@Column(nullable = false, length = 50)
	private String city;

	@Column(nullable = false, length = 50)
	private String state;

	@Column(nullable = false, length = 50)
	private String country;

	@Column(nullable = false, length = 6)
	private String pinCode;

	// Professional Details

	@Column(nullable = false, unique = true, length = 30)
	private String medicalRegistrationNumber;

	@Column(nullable = false, length = 100)
	private String qualification;

	@Column(nullable = false, length = 100)
	private String specialization;

	@Column(nullable = false)
	private Integer experience;

	@Column(nullable = false, length = 100)
	private String designation;

	// Department Mapping

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;

	// Doctor Status

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DoctorStatus status = DoctorStatus.AVAILABLE;

	// Doctor Availability

	@JsonIgnore
	@OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DoctorAvailability> availabilities;

	// Appointments

	@JsonIgnore
	@OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Appointment> appointments;

	// Leaves

	@JsonIgnore
	@OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DoctorLeave> leaves;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserRole userrole;

}