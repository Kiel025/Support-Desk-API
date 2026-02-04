package com.ezequiel.supportdesk_api.ticket.dto;

import com.ezequiel.supportdesk_api.ticket.TicketPriority;
import com.ezequiel.supportdesk_api.ticket.TicketStatus;
import com.ezequiel.supportdesk_api.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponseDTO(UUID id, String title, String description, User responsible, User customer, TicketStatus status, TicketPriority priority, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
