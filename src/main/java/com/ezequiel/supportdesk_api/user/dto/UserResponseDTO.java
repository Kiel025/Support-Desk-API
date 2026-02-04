package com.ezequiel.supportdesk_api.user.dto;

import com.ezequiel.supportdesk_api.user.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String email, UserRole role, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
