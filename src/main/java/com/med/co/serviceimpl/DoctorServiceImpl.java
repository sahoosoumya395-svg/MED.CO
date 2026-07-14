package com.med.co.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.response.DoctorResponseDto;
import com.med.co.entity.Doctor;
import com.med.co.entity.Role;
import com.med.co.entity.UserRole;
import com.med.co.enums.Enums.RoleType;
import com.med.co.exception.ResourceNotFoundException;
import com.med.co.repository.DoctorRepository;
import com.med.co.repository.RoleRepository;
import com.med.co.repository.UserRepository;
import com.med.co.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(DoctorRepository doctorRepository, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }

    // Register Doctor
    @Override
    public DoctorResponseDto registerDoctor(DoctorRegistrationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (doctorRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new RuntimeException("Mobile number already exists");
        }

        if (doctorRepository.existsByMedicalRegistrationNumber(request.getMedicalRegistrationNumber())) {
            throw new RuntimeException("Medical Registration Number already exists");
        }

        Role role = roleRepository.findByRoleName(RoleType.DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor role not found"));

        UserRole user = new UserRole();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRole(role);

        userRepository.save(user);

        Doctor doctor = modelMapper.map(request, Doctor.class);

        doctor.setPassword(passwordEncoder.encode(request.getPassword()));

        Doctor savedDoctor = doctorRepository.save(doctor);

        return modelMapper.map(savedDoctor, DoctorResponseDto.class);
    }

    // Get Doctor By Id
    @Override
    public DoctorResponseDto getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return modelMapper.map(doctor, DoctorResponseDto.class);
    }

    // Get All Doctors
    @Override
    public List<DoctorResponseDto> getAllDoctors() {

        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDto.class))
                .toList();
    }

    // Update Doctor
    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorRegistrationRequest request) {

        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Copy all matching fields from request to existing entity
        modelMapper.map(request, existingDoctor);

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);

        return modelMapper.map(updatedDoctor, DoctorResponseDto.class);
    }

    // Delete Doctor
    @Override
    public String deleteDoctor(Long id) { 

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctorRepository.delete(doctor);

        return "Doctor deleted successfully";
    }
}