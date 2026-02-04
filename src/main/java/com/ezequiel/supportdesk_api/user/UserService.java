package com.ezequiel.supportdesk_api.user;

import com.ezequiel.supportdesk_api.exceptions.UserNotFoundException;
import com.ezequiel.supportdesk_api.user.dto.UserRequestDTO;
import com.ezequiel.supportdesk_api.user.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO getById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Error: User with id " + id + " not found."));
        return convertUser2UserResponseDTO(user);
    }

    public UserResponseDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Error: User with email " + email + " not found."));
        return convertUser2UserResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertUser2UserResponseDTO).toList();
    }

    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.name());
        user.setEmail(userRequestDTO.email());
        user.setRole(userRequestDTO.role());
        user.setPasswordHash(userRequestDTO.password());

        return convertUser2UserResponseDTO(userRepository.save(user));
    }

    private UserResponseDTO convertUser2UserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.isActive(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
