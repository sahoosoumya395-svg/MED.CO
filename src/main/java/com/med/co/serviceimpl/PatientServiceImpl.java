package com.med.co.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.PatientRegistrationRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.PatientResponseDto;
import com.med.co.entity.Patient;
import com.med.co.entity.Role;
import com.med.co.entity.UserRole;
import com.med.co.enums.Enums.RoleType;
import com.med.co.repository.PatientRepository;
import com.med.co.repository.RoleRepository;
import com.med.co.repository.UserRepository;
import com.med.co.service.PatientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<?> registerPatient(PatientRegistrationRequest request) {

        try {

            if (userRepository.existsByEmail(request.getEmail())) {
                return new ApiResponse<>(
                        400,
                        "Email already exists",
                        null);
            }

            Role role = roleRepository.findByRoleName(RoleType.PATIENT)
                    .orElseThrow(() -> new RuntimeException("Patient role not found"));

            UserRole userRole = new UserRole();
            userRole.setEmail(request.getEmail());
            userRole.setPassword(request.getPassword()); // Encode later using BCrypt
            userRole.setEnabled(true);
            userRole.setRole(role);

            UserRole savedUser = userRepository.save(userRole);

            Patient patient = modelMapper.map(request, Patient.class);
            patient.setUserrole(savedUser);

            Patient savedPatient = patientRepository.save(patient);

            PatientResponseDto responseDto =
                    modelMapper.map(savedPatient, PatientResponseDto.class);

            return new ApiResponse<>(
                    201,
                    "Patient Registered Successfully",
                    responseDto);

        } catch (Exception e) {

            return new ApiResponse<>(
                    500,
                    e.getMessage(),
                    null);
        }
    }

    @Override
    public ApiResponse<?> getAllPatients() {

        try {

            List<Patient> patients = patientRepository.findAll();

            List<PatientResponseDto> responseDtos = patients.stream()
            		
                    .map(patient -> modelMapper.map(patient, PatientResponseDto.class))
                    .toList();

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
                    .orElseThrow(() -> new RuntimeException("Patient Not Found"));

            PatientResponseDto responseDto =
                    modelMapper.map(patient, PatientResponseDto.class);

            return new ApiResponse<>(
                    200,
                    "Patient Found",
                    responseDto);

        } catch (Exception e) {

            return new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null);
        }
    }
}