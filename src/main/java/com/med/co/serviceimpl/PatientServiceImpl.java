package com.med.co.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.PatientRegistrationRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.PatientResponseDto;
import com.med.co.entity.Patient;
import com.med.co.entity.Role;
import com.med.co.entity.UserRole;
import com.med.co.enums.Enums.RoleType;
import com.med.co.exception.ResourceNotFoundException;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<?> registerPatient(PatientRegistrationRequest request) {

        try {

            if (userRepository.existsByEmail(request.getEmail())) {
                return new ApiResponse<>(400, "Email already exists", null);
            }

            Role role = roleRepository.findByRoleName(RoleType.PATIENT)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient role not found"));

            UserRole user = new UserRole();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEnabled(true);
            user.setRole(role);

            UserRole savedUser = userRepository.save(user);

            Patient patient = modelMapper.map(request, Patient.class);
            patient.setUserrole(savedUser);

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
    public ApiResponse<?> getAllPatients() {

        try {

            List<Patient> patients = patientRepository.findAll();

            List<PatientResponseDto> responseDtos = patients.stream()
                    .map(patient -> modelMapper.map(patient, PatientResponseDto.class))
                    .toList();

            return new ApiResponse<>(200,
                    "Patient List",
                    responseDtos);

        } catch (Exception e) {

            return new ApiResponse<>(500,
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

            patient.setFirstName(request.getFirstName());
            patient.setMiddleName(request.getMiddleName());
            patient.setLastName(request.getLastName());
            patient.setGender(request.getGender());
            patient.setDateOfBirth(request.getDateOfBirth());
            patient.setBloodGroup(request.getBloodGroup());
            patient.setNationality(request.getNationality());
            patient.setProfilePhoto(request.getProfilePhoto());
            patient.setMobileNumber(request.getMobileNumber());
            patient.setAlternateNumber(request.getAlternateNumber());
            patient.setAddress(request.getAddress());
            patient.setCity(request.getCity());
            patient.setState(request.getState());
            patient.setCountry(request.getCountry());
            patient.setPinCode(request.getPinCode());
            patient.setHeight(request.getHeight());
            patient.setWeight(request.getWeight());
            patient.setVisionStatus(request.getVisionStatus());
            patient.setBodyTemperature(request.getBodyTemperature());
            patient.setHearingStatus(request.getHearingStatus());

            UserRole user = patient.getUserrole();
            user.setEmail(request.getEmail());

            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            userRepository.save(user);

            Patient updatedPatient = patientRepository.save(patient);

            PatientResponseDto responseDto =
                    modelMapper.map(updatedPatient, PatientResponseDto.class);

            return new ApiResponse<>(200,
                    "Patient Updated Successfully",
                    responseDto);

        } catch (Exception e) {

            return new ApiResponse<>(500,
                    e.getMessage(),
                    null);
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
}