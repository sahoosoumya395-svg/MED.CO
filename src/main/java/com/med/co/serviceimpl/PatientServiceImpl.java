package com.med.co.serviceimpl;

import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.DepartmentPatientCountRequest;
import com.med.co.dto.request.PatientRegistrationRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.DepartmentPatientCountResponse;
import com.med.co.dto.response.PatientResponseDto;
import com.med.co.entity.Patient;
import com.med.co.entity.Role;
import com.med.co.entity.UserRole;
import com.med.co.enums.Enums.RoleType;
import com.med.co.exception.ResourceNotFoundException;
import com.med.co.repository.DepartmentRepository;
import com.med.co.repository.PatientRepository;
import com.med.co.repository.RoleRepository;
import com.med.co.repository.UserRepository;
import com.med.co.service.PatientService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Random;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public ApiResponse<?> registerPatient(PatientRegistrationRequest request) {

        try {

            if (userRepository.existsByEmail(request.getEmail())) {
                return new ApiResponse<>(400, "Email already exists", null);
            }

            Role role = roleRepository.findByRoleName(RoleType.PATIENT)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient role not found"));

            // Create Patient first (before saving UserRole)
            Patient patient = modelMapper.map(request, Patient.class);

            // Create UserRole but DON'T save yet
            UserRole userrole = new UserRole();
            userrole.setEmail(request.getEmail());
            userrole.setPassword(passwordEncoder.encode(request.getPassword()));
            userrole.setEnabled(true);
            userrole.setRole(role);

            // Save UserRole to get the ID
            UserRole savedUser = userRepository.save(userrole);

            // Link the saved UserRole to patient
            patient.setUserrole(savedUser);

            // Generate MRN
            String mrnNo = generateMrnNo();
            patient.setMrnNo(mrnNo);
            // Save patient - only now after UserRole is successfully saved
            Patient savedPatient = patientRepository.save(patient);

            PatientResponseDto responseDto =
                    modelMapper.map(savedPatient, PatientResponseDto.class);

            return new ApiResponse<>(201,
                    "Patient Registered Successfully",
                    responseDto);

        } catch (Exception e) {

            return new ApiResponse<>(500,
                    e.getMessage(),
                    null);
        }
    }

    @Override
    public ApiResponse<?> getAllPatients(int page, int size, String sortBy, String direction) {

        try {

            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Patient> patientPage = patientRepository.findAll(pageable);

            Page<PatientResponseDto> responseDtos = patientPage.map(
                    patient -> modelMapper.map(patient, PatientResponseDto.class));

            return new ApiResponse<>(
                    200,
                    "Patient List",
                    responseDtos);

        } catch (Exception e) {

            return new ApiResponse<>(
                    500,
                    e.getMessage(),
                    null);
        }
    }
    @Override
    public ApiResponse<?> getPatientById(Long patientId) {

        try {

            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient Not Found"));

            PatientResponseDto responseDto =
                    modelMapper.map(patient, PatientResponseDto.class);

            return new ApiResponse<>(200,
                    "Patient Found",
                    responseDto);

        } catch (Exception e) {

            return new ApiResponse<>(404,
                    e.getMessage(),
                    null);
        }
    }

    @Override
    public ApiResponse<?> updatePatient(Long patientId, PatientRegistrationRequest request) {

        try {

            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient Not Found"));

            // Maps matching fields from request to patient
            modelMapper.map(request, patient);

            UserRole user = patient.getUserrole();

            // Map matching fields (e.g. email)
            modelMapper.map(request, user);

            // Password should be handled manually because it needs encoding
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            userRepository.save(user);
            patientRepository.save(patient);

            PatientResponseDto responseDto =
                    modelMapper.map(patient, PatientResponseDto.class);

            return new ApiResponse<>(
                    200,
                    "Patient updated successfully",
                    responseDto
            );
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ApiResponse<?> countAllPatients() {
        try {
            long count = patientRepository.count();
            return new ApiResponse<>(200, "Total patient count", count);
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<?> countPatientsByDepartment(DepartmentPatientCountRequest request) {
        try {
            if (!departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
                return new ApiResponse<>(404, "Department '" + request.getDepartmentName() + "' not found", null);
            }

            long count = patientRepository.countPatientsByDepartmentName(request.getDepartmentName());
            DepartmentPatientCountResponse response = new DepartmentPatientCountResponse(
                    request.getDepartmentName(),
                    count
            );

            return new ApiResponse<>(200, "Department patient count retrieved successfully", response);
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<?> deletePatient(Long patientId) {

        try {

            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient Not Found"));

            UserRole user = patient.getUserrole();

            patientRepository.delete(patient);

            if (user != null) {
                userRepository.delete(user);
            }

            return new ApiResponse<>(200,
                    "Patient Deleted Successfully",
                    null);

        } catch (Exception e) {

            return new ApiResponse<>(500,
                    e.getMessage(),
                    null);
        }
    }
    
    
    
    
    private String generateMrnNo() {

        Random random = new Random();
        String mrnNo;

        do {
            mrnNo = "MRN" + (100000 + random.nextInt(900000));
        } while (patientRepository.existsByMrnNo(mrnNo));

        return mrnNo;
    }
}