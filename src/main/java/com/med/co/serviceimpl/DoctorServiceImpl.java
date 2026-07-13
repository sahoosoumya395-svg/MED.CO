package com.med.co.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.response.DoctorResponseDto;
import com.med.co.entity.Doctor;
import com.med.co.repository.DoctorRepository;
import com.med.co.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DoctorResponseDto registerDoctor(DoctorRegistrationRequest request) {

        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (doctorRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new RuntimeException("Mobile number already exists");
        }

        if (doctorRepository.existsByMedicalRegistrationNumber(request.getMedicalRegistrationNumber())) {
            throw new RuntimeException("Medical Registration Number already exists");
        }

        Doctor doctor = modelMapper.map(request, Doctor.class);

        Doctor savedDoctor = doctorRepository.save(doctor);

        return modelMapper.map(savedDoctor, DoctorResponseDto.class);
    }

    @Override
    public DoctorResponseDto getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return modelMapper.map(doctor, DoctorResponseDto.class);
    }
}