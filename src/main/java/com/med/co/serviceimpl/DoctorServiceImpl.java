package com.med.co.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;
import com.med.co.dto.response.DoctorResponseDto;
import com.med.co.entity.Department;
import com.med.co.entity.Doctor;
import com.med.co.entity.Role;
import com.med.co.entity.UserRole;
import com.med.co.enums.Enums.RoleType;
import com.med.co.exception.ResourceNotFoundException;
import com.med.co.repository.DepartmentRepository;
import com.med.co.repository.DoctorRepository;
import com.med.co.repository.RoleRepository;
import com.med.co.repository.UserRepository;
import com.med.co.service.DoctorService;


@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(
            DoctorRepository doctorRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            DepartmentRepository departmentRepository,
            ModelMapper modelMapper,
            PasswordEncoder passwordEncoder) {

        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
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

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Doctor doctor = modelMapper.map(request, Doctor.class);
        doctor.setDepartment(department);
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));

        Doctor savedDoctor = doctorRepository.save(doctor);

        return modelMapper.map(savedDoctor, DoctorResponseDto.class);
    }

    // Get Doctor By Id
    @Override
    public DoctorResponseDto getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return modelMapper.map(doctor, DoctorResponseDto.class);
    }

    // Get All Doctors
    @Override
    public Page<DoctorResponseDto> getAllDoctors(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);

        return doctorPage.map(doctor -> modelMapper.map(doctor, DoctorResponseDto.class));
    }

    // Update Doctor
    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorRegistrationRequest request) {

        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        modelMapper.map(request, existingDoctor);

        existingDoctor.setDepartment(department);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingDoctor.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);

        return modelMapper.map(updatedDoctor, DoctorResponseDto.class);
    }

    // Delete Doctor
    @Override
    public String deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        doctorRepository.delete(doctor);

        return "Doctor deleted successfully";
    }
<<<<<<< HEAD

	@Override
	public DoctorLeaveResponseDto updateLeaveStatus(Long leaveId, LeaveStatusRequestDto request) {
		// TODO Auto-generated method stub
		return null;
	}
    
=======
    @Override
    public long getTotalDoctors() {

        return doctorRepository.count();

    }
>>>>>>> f0115cec8e81bf178ccbb127e55ee5d1a98b9a52
}