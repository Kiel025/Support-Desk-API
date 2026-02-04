package com.ezequiel.supportdesk_api.ticket;

import com.ezequiel.supportdesk_api.exceptions.TicketNotFoundException;
import com.ezequiel.supportdesk_api.ticket.dto.TicketResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public TicketResponseDTO getById(UUID id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException("Error: Ticket with id " + id + "not found."));
        return convertTicket2TicketResponseDTO(ticket);
    }

    private TicketResponseDTO convertTicket2TicketResponseDTO(Ticket ticket) {
        return new TicketResponseDTO(ticket.getId(), ticket.getTitle(), ticket.getDescription(),
                ticket.getResponsible(), ticket.getCustomer(), ticket.getStatus(),
                ticket.getPriority(), ticket.getCreatedAt(), ticket.getUpdatedAt());
    }
}
