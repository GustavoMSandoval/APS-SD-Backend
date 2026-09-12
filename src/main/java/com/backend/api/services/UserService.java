package com.backend.api.services;

import com.backend.api.dtos.user.UserRequestDTO;
import com.backend.api.dtos.user.UserResponseDTO;
import com.backend.api.entities.Department;
import com.backend.api.entities.User;
import com.backend.api.repositories.DepartmentRepository;
import com.backend.api.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public com.backend.api.dtos.user.UserResponseDTO create(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado."));
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setDepartment(department);
        return toResponse(userRepository.save(user));
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return toResponse(user);
    }

    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado."));
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setDepartment(department);
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        userRepository.deleteById(id);
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getDepartment().getId(), user.getDepartment().getName());
    }
}