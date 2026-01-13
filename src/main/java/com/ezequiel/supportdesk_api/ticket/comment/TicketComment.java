package com.ezequiel.supportdesk_api.ticket.comment;

import com.ezequiel.supportdesk_api.ticket.Ticket;
import com.ezequiel.supportdesk_api.ticket.TicketStatus;
import com.ezequiel.supportdesk_api.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TicketComment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 2000)
    private String comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
