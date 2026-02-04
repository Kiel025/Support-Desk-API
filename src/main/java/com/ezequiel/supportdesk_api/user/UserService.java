package com.ezequiel.supportdesk_api.user;

import com.ezequiel.supportdesk_api.exceptions.UserNotFoundException;
import com.ezequiel.supportdesk_api.user.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private UserResponseDTO convertUser2UserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.isActive(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
