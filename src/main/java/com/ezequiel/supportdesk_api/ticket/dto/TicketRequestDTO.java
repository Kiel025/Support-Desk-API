package com.ezequiel.supportdesk_api.ticket.dto;

import com.ezequiel.supportdesk_api.ticket.enums.TicketPriority;
import com.ezequiel.supportdesk_api.ticket.enums.TicketStatus;
import com.ezequiel.supportdesk_api.user.User;

public record TicketRequestDTO(String title, String description, User responsible, User customer, TicketStatus ticketStatus, TicketPriority  ticketPriority) {
}
