package com.med.co.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.request.DoctorUpdateRequest;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;
import com.med.co.dto.response.DoctorResponseDto;
import com.med.co.entity.Department;
import com.med.co.entity.Doctor;
import com.med.co.entity.Role;
import com.med.co.entity.UserRole;
import com.med.co.enums.Enums.DoctorStatus;
import com.med.co.enums.Enums.RoleType;
import com.med.co.exception.BadRequestException;
import com.med.co.exception.ResourceAlreadyExistsException;
import com.med.co.exception.ResourceNotFoundException;
import com.med.co.repository.DepartmentRepository;
import com.med.co.repository.DoctorRepository;
import com.med.co.repository.RoleRepository;
import com.med.co.repository.UserRepository;
import com.med.co.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${doctor.registration.code}")
    private Integer registrationAccessCode;

    // =========================
    // Register Doctor
    // =========================
    @Transactional
    @Override
    public DoctorResponseDto registerDoctor(DoctorRegistrationRequest request) {

        // Email validation
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        // Mobile validation
        if (doctorRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new ResourceAlreadyExistsException("Mobile number already exists");
        }

        // Medical registration validation
        if (doctorRepository.existsByMedicalRegistrationNumber(
                request.getMedicalRegistrationNumber())) {

            throw new ResourceAlreadyExistsException(
                    "Medical registration number already exists");
        }

        // Alternate mobile validation
        if (request.getAlternateMobileNumber() != null
                && !request.getAlternateMobileNumber().isBlank()
                && request.getMobileNumber()
                        .equals(request.getAlternateMobileNumber())) {

            throw new BadRequestException(
                    "Alternate mobile number cannot be same as primary mobile number");
        }

        // Registration access code validation
        if (registrationAccessCode == null
                || !registrationAccessCode.equals(request.getRegistrationCode())) {

            throw new BadRequestException("Invalid Registration Access Code");
        }

        // Find DOCTOR role
        Role role = roleRepository.findByRoleName(RoleType.DOCTOR)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor role not found"));

        // Find department
        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        // =========================
        // CREATE USER
        // =========================
        UserRole user = new UserRole();

        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setEnabled(true);
        user.setRole(role);

        UserRole savedUser = userRepository.save(user);

        // =========================
        // CREATE DOCTOR
        // =========================
        Doctor doctor = new Doctor();

        doctor.setFirstName(request.getFirstName().trim());

        doctor.setMiddleName(
                request.getMiddleName() == null
                        ? null
                        : request.getMiddleName().trim()
        );

        doctor.setLastName(request.getLastName().trim());

        doctor.setGender(request.getGender());
        doctor.setDateOfBirth(request.getDateOfBirth());
        doctor.setBloodGroup(request.getBloodGroup());

        doctor.setNationality(
                request.getNationality() == null
                        ? null
                        : request.getNationality().trim()
        );

        doctor.setMobileNumber(request.getMobileNumber());

        doctor.setAlternateMobileNumber(
                request.getAlternateMobileNumber()
        );

        doctor.setEmail(
                request.getEmail().trim().toLowerCase()
        );

        doctor.setAddress(request.getAddress().trim());
        doctor.setCity(request.getCity().trim());
        doctor.setState(request.getState().trim());
        doctor.setCountry(request.getCountry().trim());

        doctor.setPinCode(request.getPinCode());

        doctor.setMedicalRegistrationNumber(
                request.getMedicalRegistrationNumber().trim()
        );

        doctor.setQualification(
                request.getQualification().trim()
        );

        doctor.setSpecialization(
                request.getSpecialization().trim()
        );

        doctor.setExperience(request.getExperience());

        doctor.setDesignation(
                request.getDesignation().trim()
        );

        doctor.setNationality(
                request.getNationality() == null
                        ? null
                        : request.getNationality().trim()
        );

        doctor.setRegistrationCode(
                request.getRegistrationCode()
        );

        // Link department
        doctor.setDepartment(department);

        // Default doctor status
        doctor.setStatus(DoctorStatus.AVAILABLE);

        // Link Doctor with User
        doctor.setUserrole(savedUser);

        // Save Doctor
        Doctor savedDoctor = doctorRepository.save(doctor);

        // Return response
        return mapDoctorToResponse(savedDoctor);
    }

    // =========================
    // Get Doctor By Id
    // =========================
    @Override
    public DoctorResponseDto getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

        return mapDoctorToResponse(doctor);
    }

    // =========================
    // Common Response Mapping
    // =========================
    private DoctorResponseDto mapDoctorToResponse(Doctor doctor) {

        DoctorResponseDto response =
                modelMapper.map(doctor, DoctorResponseDto.class);

        if (doctor.getDepartment() != null) {

            response.setDepartmentId(
                    doctor.getDepartment().getDepartmentId()
            );

            response.setDepartmentName(
                    doctor.getDepartment().getDepartmentName()
            );
        }

        return response;
    }

    // =========================
    // Get All Doctors
    // Pagination + Sorting
    // =========================
    @Override
    public Page<DoctorResponseDto> getAllDoctors(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<Doctor> doctorPage =
                doctorRepository.findAll(pageable);

        return doctorPage.map(this::mapDoctorToResponse);
    }

    // =========================
    // Get Doctors By Department
    // =========================
    @Override
    public List<DoctorResponseDto> getDoctorsByDepartment(
            Long departmentId) {

        // Validate department
        departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        List<Doctor> doctors =
                doctorRepository.findByDepartmentDepartmentId(
                        departmentId
                );

        return doctors.stream()
                .map(this::mapDoctorToResponse)
                .toList();
    }

    // =========================
    // Update Doctor
    // =========================
    @Transactional
    @Override
    public DoctorResponseDto updateDoctor(
            Long id,
            DoctorUpdateRequest request) {

        Doctor existingDoctor =
                doctorRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found"
                                ));

        // Duplicate Email Validation
        if (doctorRepository.existsByEmailAndIdNot(
                request.getEmail(), id)) {

            throw new ResourceAlreadyExistsException(
                    "Email already exists"
            );
        }

        // Duplicate Mobile Validation
        if (doctorRepository.existsByMobileNumberAndIdNot(
                request.getMobileNumber(), id)) {

            throw new ResourceAlreadyExistsException(
                    "Mobile number already exists"
            );
        }

        // Duplicate Medical Registration Number
        if (doctorRepository
                .existsByMedicalRegistrationNumberAndIdNot(
                        request.getMedicalRegistrationNumber(),
                        id)) {

            throw new ResourceAlreadyExistsException(
                    "Medical registration number already exists"
            );
        }

        // Alternate mobile validation
        if (request.getAlternateMobileNumber() != null
                && !request.getAlternateMobileNumber().isBlank()
                && request.getMobileNumber()
                        .equals(request.getAlternateMobileNumber())) {

            throw new BadRequestException(
                    "Alternate mobile number cannot be same as primary mobile number"
            );
        }

        // Department validation
        Department department =
                departmentRepository.findById(
                        request.getDepartmentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found"
                        ));

        // =========================
        // Update Doctor Details
        // =========================
        existingDoctor.setFirstName(
                request.getFirstName().trim()
        );

        existingDoctor.setMiddleName(
                request.getMiddleName() == null
                        ? null
                        : request.getMiddleName().trim()
        );

        existingDoctor.setLastName(
                request.getLastName().trim()
        );

        existingDoctor.setGender(
                request.getGender()
        );

        existingDoctor.setDateOfBirth(
                request.getDateOfBirth()
        );

        existingDoctor.setBloodGroup(
                request.getBloodGroup()
        );

        existingDoctor.setNationality(
                request.getNationality() == null
                        ? null
                        : request.getNationality().trim()
        );

        existingDoctor.setMobileNumber(
                request.getMobileNumber()
        );

        existingDoctor.setAlternateMobileNumber(
                request.getAlternateMobileNumber()
        );

        existingDoctor.setEmail(
                request.getEmail().trim().toLowerCase()
        );

        existingDoctor.setAddress(
                request.getAddress().trim()
        );

        existingDoctor.setCity(
                request.getCity().trim()
        );

        existingDoctor.setState(
                request.getState().trim()
        );

        existingDoctor.setCountry(
                request.getCountry().trim()
        );

        existingDoctor.setPinCode(
                request.getPinCode()
        );

        existingDoctor.setMedicalRegistrationNumber(
                request.getMedicalRegistrationNumber().trim()
        );

        existingDoctor.setQualification(
                request.getQualification().trim()
        );

        existingDoctor.setSpecialization(
                request.getSpecialization().trim()
        );

        existingDoctor.setExperience(
                request.getExperience()
        );

        existingDoctor.setDesignation(
                request.getDesignation().trim()
        );

        existingDoctor.setRegistrationCode(
                request.getRegistrationCode()
        );

        existingDoctor.setDepartment(
                department
        );

        // Save updated doctor
        Doctor updatedDoctor =
                doctorRepository.save(existingDoctor);

        return mapDoctorToResponse(updatedDoctor);
    }

    // =========================
    // Delete Doctor
    // =========================
    @Override
    public String deleteDoctor(Long id) {

        Doctor doctor =
                doctorRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found"
                                ));

        doctorRepository.delete(doctor);

        return "Doctor deleted successfully";
    }

    // =========================
    // Update Leave Status
    // =========================
    @Override
    public DoctorLeaveResponseDto updateLeaveStatus(
            Long leaveId,
            LeaveStatusRequestDto request) {

        // TODO:
        // Implement when DoctorLeave approval workflow is added

        return null;
    }

    // =========================
    // Count Total Doctors
    // =========================
    @Override
    public long getTotalDoctors() {

        return doctorRepository.count();
    }
}