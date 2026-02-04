package com.ezequiel.supportdesk_api.user.dto;

import com.ezequiel.supportdesk_api.user.UserRole;

public record UserRequestDTO(String name, String email, String password, UserRole role) {
}
